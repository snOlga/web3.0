package lib.entity.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lib.entity.dto.entity.BannedWordEntity;

@Repository
public interface BannedWordsRepository extends JpaRepository<BannedWordEntity, Long> {
    
}
