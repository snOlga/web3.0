package web.ozon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.*;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import web.ozon.DTO.CommentReportDTO;
import web.ozon.converter.CommentReportConverter;
import web.ozon.entity.CommentEntity;
import web.ozon.entity.CommentReportEntity;
import web.ozon.entity.UserEntity;
import web.ozon.exception.CommentReportNotExistException;
import web.ozon.repository.CommentReportRepository;
import web.ozon.repository.CommentRepository;
import web.ozon.repository.UserRepository;

@Service
public class CommentReportService {

    @Autowired
    private CommentReportRepository commentReportRepository;
    @Autowired
    private CommentReportConverter commentReportConverter;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;
    private DefaultTransactionDefinition definition;

    @PostConstruct
    public void init() {
        definition = new DefaultTransactionDefinition();
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        definition.setTimeout(3);
    }

    @Value("${business.pagination.step}")
    private int PAGINATION_STEP;

    public List<CommentReportDTO> getAll(int from) {
        return commentReportRepository.findAll(PageRequest.of(from, PAGINATION_STEP)).getContent().stream()
                .map(commentReportConverter::fromEntity).toList();
    }

    public List<CommentReportDTO> getAllNotChecked(int from) {
        return getAll(from).stream().filter(report -> report.getIsAccepted() == null).toList();
    }

    public List<CommentReportDTO> getAllByCommentId(Long id, int from) {
        return commentReportRepository.findAllByCommentId(id, PageRequest.of(from, PAGINATION_STEP)).stream()
                .map(commentReportConverter::fromEntity).toList();
    }

    public CommentReportDTO save(CommentReportDTO dto) {
        return commentReportConverter.fromEntity(commentReportRepository.save(commentReportConverter.fromDTO(dto)));
    }

    // @Transactional
    public CommentReportDTO updateByChecker(CommentReportDTO dto) throws CommentReportNotExistException {
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        CommentReportEntity entity = commentReportRepository.findById(dto.getId()).orElse(null);
        if (entity == null) {
            transactionManager.rollback(transaction);
            throw new CommentReportNotExistException();
        }

        setCheckerAndAccepted(dto, entity);
        if (dto.getIsAccepted()) {
            removeReportedComment(dto);
        }
        commentReportRepository.save(entity);
        transactionManager.commit(transaction);

        return commentReportConverter.fromEntity(entity);
    }

    private void setCheckerAndAccepted(CommentReportDTO dto, CommentReportEntity entity) {
        UserEntity checker = userRepository.findByLogin(
                SecurityContextHolder.getContext().getAuthentication().getName());
        entity.setChecker(checker);
        entity.setIsAccepted(dto.getIsAccepted());
    }

    // @Transactional
    private void removeReportedComment(CommentReportDTO dto) {
        CommentEntity commentEntity = commentRepository.findById(dto.getCommentId()).get();
        commentEntity.setIsReported(true);
        commentRepository.save(commentEntity);
    }

    public CommentReportDTO updateByUser(CommentReportDTO dto) {
        return save(dto);
    }
}
