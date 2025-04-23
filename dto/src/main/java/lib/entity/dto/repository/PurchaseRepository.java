package lib.entity.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lib.entity.dto.entity.PurchaseEntity;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {
    
    public PurchaseEntity findByOwnerIdAndProductId(Long ownerId, Long productId);
}
