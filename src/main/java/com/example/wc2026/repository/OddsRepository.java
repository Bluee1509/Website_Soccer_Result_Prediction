package com.example.wc2026.repository;

import com.example.wc2026.model.Odds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OddsRepository extends JpaRepository<Odds, Long> {

    List<Odds> findByMatchId(Long matchId);


    Optional<Odds> findByMatchIdAndOddTypeAndChoice(Long matchId, String oddType, String choice);
}