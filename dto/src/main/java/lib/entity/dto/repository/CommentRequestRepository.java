package lib.entity.dto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lib.entity.dto.entity.CommentRequestEntity;

@Repository
public interface CommentRequestRepository extends JpaRepository<CommentRequestEntity, Long> {

    public List<CommentRequestEntity> findByIsChecked(Boolean isChecked);
}
