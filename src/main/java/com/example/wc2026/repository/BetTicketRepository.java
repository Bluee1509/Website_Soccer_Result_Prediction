package com.example.wc2026.repository;

import com.example.wc2026.model.BetTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BetTicketRepository extends JpaRepository<BetTicket, Long> {
    List<BetTicket> findByUserId(Long userId);
    List<BetTicket> findByMatchIdAndStatus(Long matchId, String status);
    List<BetTicket> findByUserUsernameOrderByCreatedAtDesc(String username);

}