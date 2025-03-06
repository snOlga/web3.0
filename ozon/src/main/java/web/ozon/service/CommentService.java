package web.ozon.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

import web.ozon.DTO.CommentDTO;
import web.ozon.converter.CommentConverter;
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
    private BannedWordsRepository bannedWordsRepository;
    @Autowired
    private CommentRequestService commentRequestService;

    public List<CommentDTO> getAllByProductId(Long productId, int from, int to) {
        return commentRepository.findAllByProductId(productId, PageRequest.of(from, to)).stream()
                .filter(comment -> comment.getIsChecked()).map(commentConverter::fromEntity).toList();
    }

    @Transactional
    public CommentDTO save(CommentDTO commentDTO) {
        if (!isBusinessOk(commentDTO))
            return null;
        CommentEntity commentEntity = commentConverter.fromDTO(commentDTO);
        commentRepository.save(commentEntity);
        CommentDTO result = commentConverter.fromEntity(commentEntity);
        commentRequestService.createRequest(result);
        return result;
    }

    private boolean isBusinessOk(CommentDTO commentDTO) {
        return isCommentNew(commentDTO)
                && isTheProductWasBought(commentDTO)
                && !isRudeText(commentDTO.getContent());
    }

    private boolean isCommentNew(CommentDTO commentDTO) {
        CommentEntity existedComment = commentRepository.findByProductIdAndAuthorId(commentDTO.getProductId(),
                commentDTO.getAuthorId());
        return existedComment == null;
    }

    private boolean isTheProductWasBought(CommentDTO commentDTO) {
        PurchaseEntity purchaseEntity = purchaseRepository.findByOwnerIdAndProductId(commentDTO.getAuthorId(),
                commentDTO.getProductId());
        return purchaseEntity != null;
    }

    private int BANNED_WORDS_STEP = 10;

    private boolean isRudeText(String input) {
        return Stream.iterate(0, from -> from + BANNED_WORDS_STEP)
                .map(from -> bannedWordsRepository.findAll(PageRequest.of(from, BANNED_WORDS_STEP)))
                .takeWhile(bannedWords -> !bannedWords.isEmpty())
                .flatMap(bannedWords -> bannedWords.getContent().stream())
                .map(BannedWordEntity::getWord)
                .anyMatch(input::contains);
    }

    @Transactional
    public CommentDTO update(CommentDTO commentDTO) {
        CommentEntity existing = commentRepository.findById(commentDTO.getId())
                .orElse(null);
        if (existing == null || existing.getIsDeleted() || !isAuthorTheSame(existing)) {
            return null;
        }

        if (!isBusinessOk(commentDTO)) // TODO: wrong logic
            return null;

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
        if (comment == null || comment.getIsDeleted())
            return false;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ADMIN"));
        boolean isOwner = comment.getAuthor().getLogin().equals(auth.getName());

        if (!isOwner && !isAdmin)
            return false;

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
