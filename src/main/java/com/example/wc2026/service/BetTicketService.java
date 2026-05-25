package com.example.wc2026.service;

import com.example.wc2026.dto.BetTicketRequest;
import com.example.wc2026.model.*;
import com.example.wc2026.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BetTicketService {

    @Autowired private BetTicketRepository betTicketRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private MatchRepository matchRepository;
    @Autowired private OddsRepository oddsRepository;
    @Autowired private TransactionService transactionService; // Nhất quán dùng Service

    @Transactional
    public BetTicket placeBet(BetTicketRequest request, String username) {

        User user = userRepository.findByUsernameForUpdate(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trận đấu!"));

        if (!"PENDING".equals(match.getStatus())) {
            throw new RuntimeException("Trận đấu đã diễn ra hoặc kết thúc!");
        }

        Odds odds = oddsRepository.findById(request.getOddsId())
                .orElseThrow(() -> new RuntimeException("Tỷ lệ cược không hợp lệ!"));


        if (user.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Số dư không đủ!");
        }


        user.setBalance(user.getBalance().subtract(request.getAmount()));
        userRepository.save(user);


        transactionService.logTransaction(user, "BET", request.getAmount().negate());


        return betTicketRepository.save(BetTicket.builder()
                .user(user)
                .match(match)
                .odds(odds)
                .amount(request.getAmount())
                .potentialWin(request.getAmount().multiply(odds.getRate()))
                .status("PENDING")
                .createdAt(java.time.LocalDateTime.now())
                .build());
    }

    @Transactional
    public void resolveMatchBets(Match match) {
        List<BetTicket> pendingTickets = betTicketRepository.findByMatchIdAndStatus(match.getId(), "PENDING");

        for (BetTicket ticket : pendingTickets) {
            boolean isWinner = checkWinner(ticket, match);

            if (isWinner) {
                ticket.setStatus("WON");


                User user = userRepository.findByIdForUpdate(ticket.getUser().getId())
                        .orElseThrow(() -> new RuntimeException("User không tồn tại"));

                user.setBalance(user.getBalance().add(ticket.getPotentialWin()));
                userRepository.save(user);


                transactionService.logTransaction(user, "WIN", ticket.getPotentialWin());
            } else {
                ticket.setStatus("LOST");
            }
        }
        betTicketRepository.saveAll(pendingTickets);
    }

    private boolean checkWinner(BetTicket ticket, Match match) {
        Odds odds = ticket.getOdds();
        if ("1X2".equals(odds.getOddType())) {
            int home = match.getHomeScore();
            int away = match.getAwayScore();
            if (home > away && "HOME".equals(odds.getChoice())) return true;
            if (home < away && "AWAY".equals(odds.getChoice())) return true;
            if (home == away && "DRAW".equals(odds.getChoice())) return true;
        }
        return false;
    }
    public List<BetTicket> getMyTickets(String username) {
        return betTicketRepository.findByUserUsernameOrderByCreatedAtDesc(username);
    }
}