package web.ozon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import web.ozon.entity.CommentReportEntity;

@Repository
public interface CommentReportRepository extends JpaRepository<CommentReportEntity, Long> {

    public List<CommentReportEntity> findAllByCommentId(Long commentId, Pageable pageable);

    public CommentReportEntity findByCommentIdAndReporterId(Long commentId, Long reporterId);
}
