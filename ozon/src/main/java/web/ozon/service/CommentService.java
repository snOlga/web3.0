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
                .map(commentConverter::fromEntity).toList();
    }

    @Transactional
    public CommentDTO save(CommentDTO commentDTO) {
        if (commentDTO.getId() != null)
            return null;
        if (!isCommentOk(commentDTO))
            return null;
        if (isRudeText(commentDTO.getContent()))
            return null;

        CommentEntity commentEntity = commentConverter.fromDTO(commentDTO);
        commentRepository.save(commentEntity);
        CommentDTO result = commentConverter.fromEntity(commentEntity);
        commentRequestService.createRequest(result);
        return result;
    }

    private boolean isCommentOk(CommentDTO commentDTO) {
        if (!isCommentFieldsOk(commentDTO))
            return false;
        if (!isAuthorTheSame(commentDTO))
            return false;
        commentDTO.setAuthor(userConverter.fromEntity(userConverter.fromDTO(commentDTO.getAuthor())));
        if (!isCommentNew(commentDTO))
            return false;
        if (!isTheProductWasBought(commentDTO))
            return false;
        return true;
    }

    private boolean isCommentFieldsOk(CommentDTO commentDTO) {
        return (commentDTO != null) && (commentDTO.getAuthor().getId() != null);
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
}
