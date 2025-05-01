package web.ozon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.security.core.Authentication;

import lib.entity.dto.DTO.CommentDTO;
import lib.entity.dto.converter.CommentConverter;
import lib.entity.dto.entity.CommentEntity;
import lib.entity.dto.exception.*;
import lib.entity.dto.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentConverter commentConverter;
    @Autowired
    private CommentRequestService commentRequestService;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private DefaultTransactionDefinition definition;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${business.pagination.step}")
    private int PAGINATION_STEP;
    @Value(value = "${kafka.custom.topicname.comment}")
    private String topicCommentsName;

    public List<CommentDTO> getAllByProductId(Long productId, int from) {
        sendMessage("hiiii");
        return commentRepository.findAllByProductId(productId, PageRequest.of(from, PAGINATION_STEP)).stream()
                .filter(comment -> comment.getIsChecked())
                .filter(comment -> !comment.getIsReported() || comment.getIsReported() == null)
                .map(commentConverter::fromEntity).toList();
    }

    public CommentDTO save(CommentDTO commentDTO) throws CommentNotNewException, NonNullNewIdException {
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        try {
            isCommentNew(commentDTO);
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw e;
        }
        CommentDTO result = saveNoTrasaction(commentDTO);
        transactionManager.commit(transaction);
        return result;
    }

    public void sendMessage(String msg) {
        kafkaTemplate.send(topicCommentsName, msg);
    }

    private CommentDTO saveNoTrasaction(CommentDTO commentDTO) {
        CommentEntity commentEntity = commentConverter.fromDTO(commentDTO);
        commentRepository.save(commentEntity);
        CommentDTO result = commentConverter.fromEntity(commentEntity);
        commentRequestService.createRequest(result);
        return result;
    }

    private void isCommentNew(CommentDTO commentDTO) throws CommentNotNewException, NonNullNewIdException {
        if (commentDTO.getId() != null)
            throw new NonNullNewIdException();
        CommentEntity existedComment = commentRepository.findByProductIdAndAuthorId(commentDTO.getProductId(),
                commentDTO.getAuthorId());
        if (existedComment != null)
            throw new CommentNotNewException();
    }

    public CommentDTO update(CommentDTO commentDTO) throws CommentNotExistException, NotSameAuthorException {
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        try {
            isAbleToUpdateOrDelete(commentDTO.getId());
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw e;
        }

        CommentDTO result = updateNoTransaction(commentDTO);

        transactionManager.commit(transaction);
        return result;
    }

    private CommentDTO updateNoTransaction(CommentDTO commentDTO) {
        CommentEntity existing = commentRepository.findById(commentDTO.getId()).get();
        if (!existing.getContent().equals(commentDTO.getContent())) {
            existing.setContent(commentDTO.getContent());
            existing.setIsChecked(false);
            commentRepository.save(existing);
            commentRequestService.createRequest(commentDTO);
        }
        return commentConverter.fromEntity(existing);
    }

    public boolean delete(Long id) throws CommentNotExistException, NotSameAuthorException {
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        try {
            isAbleToUpdateOrDelete(id);
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw e;
        }

        CommentEntity comment = commentRepository.findById(id).orElse(null);
        comment.setIsDeleted(true);
        commentRepository.save(comment);

        transactionManager.commit(transaction);
        return true;
    }

    private void isAbleToUpdateOrDelete(Long id) throws CommentNotExistException, NotSameAuthorException {
        CommentEntity comment = commentRepository.findById(id).orElse(null);
        if (comment == null)
            throw new CommentNotExistException();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ADMIN"));
        boolean isOwner = comment.getAuthor().getLogin().equals(auth.getName());

        if (!isOwner && !isAdmin)
            throw new NotSameAuthorException();
    }
}
