package web.ozon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.ozon.entity.CommentReportEntity;

@Repository
public interface CommentReportRepository extends JpaRepository<CommentReportEntity, Long> {

}
