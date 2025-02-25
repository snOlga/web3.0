package web.ozon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.ozon.entity.BannedWordEntity;

@Repository
public interface BannedWordsRepository extends JpaRepository<BannedWordEntity, Long> {
    
}
