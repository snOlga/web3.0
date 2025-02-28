package web.ozon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

import web.ozon.DTO.CommentDTO;
import web.ozon.converter.CommentConverter;
import web.ozon.converter.UserConverter;
import web.ozon.entity.BannedWordEntity;
import web.ozon.entity.CommentEntity;
import web.ozon.entity.PurchaseEntity;
import web.ozon.repository.BannedWordsRepository;
import web.ozon.repository.CommentRepository;
import web.ozon.repository.PurchaseRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentConverter commentConverter;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private BannedWordsRepository bannedWordsRepository;
    @Autowired
    private CommentRequestService commentRequestService;

    public List<CommentDTO> getAllByProductId(Long productId, int from, int to) {
        return commentRepository.findAllByProductId(productId, PageRequest.of(from, to)).stream()
                .filter(comment -> comment.getIsChecked()).map(commentConverter::fromEntity).toList();
    }

    @Transactional
    public CommentDTO save(CommentDTO commentDTO) {
        if (!isCommentOk(commentDTO))
            return null;
        CommentEntity commentEntity = commentConverter.fromDTO(commentDTO);
        commentRepository.save(commentEntity);
        CommentDTO result = commentConverter.fromEntity(commentEntity);
        commentRequestService.createRequest(result);
        return result;
    }

    private boolean isCommentOk(CommentDTO commentDTO) {
        if (!isOkDto(commentDTO))
            return false;
        commentDTO.setAuthor(userConverter.fromEntity(userConverter.fromDTO(commentDTO.getAuthor())));
        return isBusinessOk(commentDTO);
    }

    private boolean isOkDto(CommentDTO commentDTO) {
        return isCommentFieldsOk(commentDTO) && isAuthorTheSame(commentDTO);
    }

    private boolean isBusinessOk(CommentDTO commentDTO) {
        return isCommentNew(commentDTO)
                && isTheProductWasBought(commentDTO)
                && !isRudeText(commentDTO.getContent());
    }

    private boolean isCommentFieldsOk(CommentDTO commentDTO) {
        return (commentDTO != null)
                && (commentDTO.getAuthor().getId() != null)
                && commentDTO.getId() == null;
    }

    private boolean isCommentNew(CommentDTO commentDTO) {
        CommentEntity existedComment = commentRepository.findByProductIdAndAuthorId(commentDTO.getProduct().getId(),
                commentDTO.getAuthor().getId());
        return existedComment != null;
    }

    private boolean isAuthorTheSame(CommentDTO commentDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return commentDTO.getAuthor().getLogin().equals(currentPrincipalName);
    }

    private boolean isTheProductWasBought(CommentDTO commentDTO) {
        PurchaseEntity purchaseEntity = purchaseRepository.findByOwnerIdAndProductId(commentDTO.getAuthor().getId(),
                commentDTO.getProduct().getId());
        return purchaseEntity != null;
    }

    private int BANNED_WORDS_STEP = 10;

    private boolean isRudeText(String input) {
        int from = 0;
        int to = BANNED_WORDS_STEP;
        Page<BannedWordEntity> bannedWords = bannedWordsRepository.findAll(PageRequest.of(from, to));
        while (!bannedWords.isEmpty()) {
            for (BannedWordEntity bannedWordEntity : bannedWords) {
                if (input.contains(bannedWordEntity.getWord()))
                    return true;
            }
            from += BANNED_WORDS_STEP;
            to += BANNED_WORDS_STEP;
            bannedWords = bannedWordsRepository.findAll(PageRequest.of(from, to));
        }
        return false;
    }

    @Transactional
    public CommentDTO update(CommentDTO commentDTO) {
        CommentEntity existing = commentRepository.findById(commentDTO.getId())
                .orElse(null);
        if (existing == null || existing.getIsDeleted() || !isAuthorTheSame(existing)) {
            return null;
        }

        if (!isCommentOk(commentDTO)) return null;

        // Обновление контента
        if (!existing.getContent().equals(commentDTO.getContent())) {
            existing.setContent(commentDTO.getContent());
            existing.setIsChecked(false); // Требует повторной проверки
            commentRepository.save(existing);
            commentRequestService.createRequest(commentDTO);
        }
        return commentConverter.fromEntity(existing);
    }

    @Transactional
    public boolean delete(Long id) {
        CommentEntity comment = commentRepository.findById(id).orElse(null);
        if (comment == null || comment.getIsDeleted()) return false;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ADMIN"));
        boolean isOwner = comment.getAuthor().getLogin().equals(auth.getName());

        if (!isOwner && !isAdmin) return false;

        comment.setIsDeleted(true);
        commentRepository.save(comment);
        return true;
    }

    private boolean isAuthorTheSame(CommentEntity comment) {
        String currentUser = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return comment.getAuthor().getLogin().equals(currentUser);
    }
}
