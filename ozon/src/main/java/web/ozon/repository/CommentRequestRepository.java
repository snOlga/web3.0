package web.ozon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.ozon.entity.CommentRequestEntity;

@Repository
public interface CommentRequestRepository extends JpaRepository<CommentRequestEntity, Long> {

    
}
