package com.example.wc2026.controller;

import com.example.wc2026.dto.OddsRequest;
import com.example.wc2026.model.Odds;
import com.example.wc2026.service.OddsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/odds")
public class OddsController {

    @Autowired
    private OddsService oddsService;

    @PostMapping
    public ResponseEntity<?> setOdds(@Valid @RequestBody OddsRequest request) {
        try {
            Odds savedOdds = oddsService.saveOrUpdateOdds(request);
            return ResponseEntity.ok(savedOdds);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<?> getByMatchId(@PathVariable Long matchId) {
        try {
            List<Odds> oddsList = oddsService.getOddsByMatchId(matchId);
            return ResponseEntity.ok(oddsList);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}