package micro.service.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import lib.entity.dto.DTO.CommentRequestDTO;
import lib.entity.dto.converter.CommentRequestConverter;
import lib.entity.dto.entity.CommentEntity;
import lib.entity.dto.entity.CommentRequestEntity;
import lib.entity.dto.entity.UserEntity;
import lib.entity.dto.exception.NullCommentException;
import lib.entity.dto.exception.NullContentException;
import lib.entity.dto.repository.CommentRepository;
import lib.entity.dto.repository.CommentRequestRepository;
import lib.entity.dto.repository.UserRepository;

@Service
public class CommentRequestService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentRequestRepository commentRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRequestConverter commentRequestConverter;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private DefaultTransactionDefinition definition;

    @Value("${business.pagination.step}")
    private int PAGINATION_STEP;

    @KafkaListener(topics = "${kafka.custom.topicname.comment}")
    public void createRequest(Long CommentId) {
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        try {
            CommentEntity commentEntity = commentRepository.findById(CommentId).get();
            CommentRequestEntity commentRequestEntity = new CommentRequestEntity(null, commentEntity, null, null,
                    false);
            commentRequestRepository.save(commentRequestEntity);
        } catch (Exception e) {
            // TODO: handle exception
        }
        transactionManager.commit(transaction);
    }

    public List<CommentRequestDTO> getAll(int from) {
        return commentRequestRepository.findAll(PageRequest.of(from, PAGINATION_STEP)).getContent().stream()
                .map(commentRequestConverter::fromEntity).toList();
    }

    public List<CommentRequestDTO> getAllNotChecked(int from) {
        return getAll(from).stream().filter(req -> !req.getIsChecked()).toList();
    }

    public CommentRequestDTO update(CommentRequestDTO dto) throws NullCommentException, NullContentException {
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        CommentRequestDTO result = null;
        try {
            updateNoTransaction(dto);
            transactionManager.commit(transaction);
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw e;
        }
        return result;
    }

    private CommentRequestDTO updateNoTransaction(CommentRequestDTO dto)
            throws NullCommentException, NullContentException {
        CommentRequestEntity entity = commentRequestRepository.findById(dto.getId())
                .orElse(null);
        if (entity == null || entity.getIsDeleted()) {
            throw new NullCommentException();
        }

        if (dto.getIsChecked() == null) {
            throw new NullContentException();
        }

        entity.setIsChecked(dto.getIsChecked());
        UserEntity checker = userRepository.findByLogin(
                SecurityContextHolder.getContext().getAuthentication().getName());
        entity.setChecker(checker);
        if (dto.getIsChecked()) {
            entity.getComment().setIsChecked(true);
            commentRepository.save(entity.getComment());
        }

        commentRequestRepository.save(entity);

        return commentRequestConverter.fromEntity(entity);
    }

    public boolean delete(Long id) {
        CommentRequestEntity entity = commentRequestRepository.findById(id)
                .orElse(null);
        if (entity == null || entity.getIsDeleted())
            return false;
        entity.setIsDeleted(true);
        commentRequestRepository.save(entity);
        return true;
    }
}
