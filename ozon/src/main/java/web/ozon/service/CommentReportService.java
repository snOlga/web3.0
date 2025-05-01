package web.ozon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.*;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import lib.entity.dto.DTO.CommentReportDTO;
import lib.entity.dto.converter.CommentReportConverter;
import lib.entity.dto.entity.CommentEntity;
import lib.entity.dto.entity.CommentReportEntity;
import lib.entity.dto.entity.UserEntity;
import lib.entity.dto.exception.CommentReportNotExistException;
import lib.entity.dto.repository.*;

@Service
public class  CommentReportService {

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
    @Autowired
    private DefaultTransactionDefinition definition;

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

    public CommentReportDTO updateByChecker(CommentReportDTO dto) throws CommentReportNotExistException {
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        Long id = dto.getId();
        CommentReportEntity entity = commentReportRepository.findById(id).orElse(null);
        if (entity == null) {
            transactionManager.rollback(transaction);
            throw new CommentReportNotExistException();
        }

        setCheckerAndAccepted(dto, entity);
        if (dto.getIsAccepted()) {
            removeReportedComment(commentReportConverter.fromEntity(entity));
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

    private void removeReportedComment(CommentReportDTO dto) {
        CommentEntity commentEntity = commentRepository.findById(dto.getCommentId()).get();
        commentEntity.setIsReported(true);
        commentRepository.save(commentEntity);
    }

    public CommentReportDTO updateByUser(CommentReportDTO dto) {
        return save(dto);
    }
}
