package com.example.wc2026.service;

import com.example.wc2026.dto.MatchRequest;
import com.example.wc2026.model.Match;
import com.example.wc2026.model.Team;
import com.example.wc2026.repository.MatchRepository;
import com.example.wc2026.repository.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID; // 🌟 Nhớ import thư viện này để tạo mã random

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private BetTicketService betTicketService;

    public Match createMatch(MatchRequest request) {
        
        if (request.getHomeTeamId().equals(request.getAwayTeamId())) {
            throw new RuntimeException("Một đội bóng không thể tự thi đấu với chính mình!");
        }


        Team homeTeam = teamRepository.findById(request.getHomeTeamId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Đội nhà với ID: " + request.getHomeTeamId()));


        Team awayTeam = teamRepository.findById(request.getAwayTeamId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Đội khách với ID: " + request.getAwayTeamId()));


        String randomMatchCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();


        Match match = Match.builder()
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .startTime(request.getStartTime())
                .homeScore(0)
                .awayScore(0)
                .status("PENDING")
                .matchCode(randomMatchCode)
                .build();

        return matchRepository.save(match);
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }


    @Transactional
    public Match updateMatchResult(Long matchId, Integer homeScore, Integer awayScore) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trận đấu!"));

        if ("FINISHED".equals(match.getStatus())) {
            throw new RuntimeException("Trận đấu này đã kết thúc và được trả thưởng rồi!");
        }


        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        match.setStatus("FINISHED");
        Match savedMatch = matchRepository.save(match);


        betTicketService.resolveMatchBets(savedMatch);

        return savedMatch;
    }

    public Page<Match> getMatchesPaged(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return matchRepository.findAll(pageable);
    }

    public Match getMatchById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trận đấu với ID: " + id));
    }
}