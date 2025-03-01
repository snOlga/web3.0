package web.ozon.repository;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import web.ozon.entity.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    public List<CommentEntity> findAllByProductId(Long productId, Pageable pageable);

    @Query("SELECT c FROM CommentEntity c WHERE c.product.id = :productId AND c.author.id = :authorId")
    CommentEntity findByProductIdAndAuthorId(@Param("productId") Long productId, @Param("authorId") Long authorId);

}
