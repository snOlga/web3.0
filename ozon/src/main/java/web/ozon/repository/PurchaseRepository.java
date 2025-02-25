package web.ozon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.ozon.entity.PurchaseEntity;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {
    
    public PurchaseEntity findByOwnerIdAndProductId(Long ownerId, Long productId);
}
