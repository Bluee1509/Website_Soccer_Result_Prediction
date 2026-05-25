package com.example.wc2026.repository;

import com.example.wc2026.model.Odds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OddsRepository extends JpaRepository<Odds, Long> {
    // Tìm tất cả các kèo của 1 trận đấu
    List<Odds> findByMatchId(Long matchId);

    // Tìm chính xác dòng kèo độc nhất để xử lý logic update
    Optional<Odds> findByMatchIdAndOddTypeAndChoice(Long matchId, String oddType, String choice);
}