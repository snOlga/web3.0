package micro.service.admin.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
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
    private static final Logger log = LoggerFactory.getLogger(CommentRequestService.class);

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
    @Autowired
    private KafkaTemplate<String, Long> kafkaTemplate;

    @Value("${business.pagination.step}")
    private int PAGINATION_STEP;
    @Value(value = "${kafka.custom.topicname.deleted_comments}")
    private String topicNameDeletedComments;

    @Autowired
    private Bitrix24JcaService bitrix24Service;

    @KafkaListener(topics = "${kafka.custom.topicname.comment}")
    public void createRequest(Long commentId) {
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        try {
            CommentEntity commentEntity = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Comment not found"));
            CommentRequestEntity commentRequestEntity = new CommentRequestEntity(null, commentEntity, null, null, false);
            commentRequestRepository.save(commentRequestEntity);

            String title = "Заявка на проверку комментария #" + commentId;
            String description = "Текст комментария: " + commentEntity.getContent();
            try {
                String result = bitrix24Service.createTask(title, description);
                log.debug("Bitrix24 task created: {}", result);
            } catch (Exception e) {
                log.error("Failed to create Bitrix24 task", e);
            }

            transactionManager.commit(transaction);
        } catch (Exception e) {
            log.error("Error creating comment request", e);
            kafkaTemplate.send(topicNameDeletedComments, commentId);
            transactionManager.rollback(transaction);
        }
    }

    private CommentRequestDTO updateNoTransaction(CommentRequestDTO dto)
            throws NullCommentException, NullContentException {
        CommentRequestEntity entity = commentRequestRepository.findById(dto.getId())
                .orElseThrow(NullCommentException::new);

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

            try {
                String result = bitrix24Service.updateTask(entity.getId().intValue(), "5");
                log.debug("Bitrix24 task updated: {}", result);
            } catch (Exception e) {
                log.error("Failed to update Bitrix24 task", e);
            }
        }

        commentRequestRepository.save(entity);
        return commentRequestConverter.fromEntity(entity);
    }

    private List<CommentRequestEntity> getAllEntity(int from) {
        return commentRequestRepository.findAll(PageRequest.of(from, PAGINATION_STEP)).getContent();
    }

    public List<CommentRequestDTO> getAll(int from) {
        return getAllEntity(from).stream().map(commentRequestConverter::fromEntity).toList();
    }

    public List<CommentRequestDTO> getAllNotChecked(int from) {
        return getAll(from).stream().filter(req -> !req.getIsChecked()).toList();
    }

    public CommentRequestDTO update(CommentRequestDTO dto) throws NullCommentException, NullContentException {
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        try {
            CommentRequestDTO result = updateNoTransaction(dto);
            transactionManager.commit(transaction);
            return result;
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw e;
        }
    }

    public boolean delete(Long id) {
        return commentRequestRepository.findById(id)
                .map(entity -> {
                    entity.setIsDeleted(true);
                    commentRequestRepository.save(entity);
                    return true;
                })
                .orElse(false);
    }

    @Scheduled(fixedDelay = 10000)
    public void scheduleFixedDelayTask() {
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        try {
            for (int i = 0; i < commentRequestRepository.count(); i++) {
                List<CommentRequestEntity> pack = getAllEntity(i);

                for (CommentRequestEntity request : pack.stream()
                        .filter(request -> request.getIsChecked() != null)
                        .toList()) {
                    request.setIsDeleted(true);
                    commentRequestRepository.save(request);

                    if (!request.getIsChecked()) {
                        kafkaTemplate.send(topicNameDeletedComments, request.getComment().getId());
                    }
                }
            }
            transactionManager.commit(transaction);
        } catch (Exception e) {
            log.error("Error in scheduled task", e);
            transactionManager.rollback(transaction);
        }
    }
}
