package com.example.wc2026.service;

import com.example.wc2026.dto.OddsRequest;
import com.example.wc2026.model.Match;
import com.example.wc2026.model.Odds;
import com.example.wc2026.repository.MatchRepository;
import com.example.wc2026.repository.OddsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OddsService {

    @Autowired
    private OddsRepository oddsRepository;

    @Autowired
    private MatchRepository matchRepository;

    public Odds saveOrUpdateOdds(OddsRequest request) {
        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trận đấu với ID: " + request.getMatchId()));

        String normalizedType = request.getOddType().toUpperCase().trim();
        String normalizedChoice = request.getChoice().toUpperCase().trim();

        Odds odds = oddsRepository.findByMatchIdAndOddTypeAndChoice(request.getMatchId(), normalizedType, normalizedChoice)
                .orElse(new Odds());

        odds.setMatch(match);
        odds.setOddType(normalizedType);
        odds.setChoice(normalizedChoice);
        odds.setRate(request.getRate());

        return oddsRepository.save(odds);
    }

    public List<Odds> getOddsByMatchId(Long matchId) {
        return oddsRepository.findByMatchId(matchId);
    }
}