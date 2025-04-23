package lib.entity.dto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lib.entity.dto.entity.CommentReportEntity;

@Repository
public interface CommentReportRepository extends JpaRepository<CommentReportEntity, Long> {

    public List<CommentReportEntity> findAllByCommentId(Long commentId, Pageable pageable);

    public CommentReportEntity findByCommentIdAndReporterId(Long commentId, Long reporterId);
}
