package com.example.wc2026.controller;

import com.example.wc2026.dto.ChangePasswordRequest;
import com.example.wc2026.dto.MatchRequest;
import com.example.wc2026.dto.MatchResultRequest;
import com.example.wc2026.model.Match;
import com.example.wc2026.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PostMapping
    public ResponseEntity<Match> createMatch(@Valid @RequestBody MatchRequest request) {

        Match newMatch = matchService.createMatch(request);
        return ResponseEntity.ok(newMatch);
    }

    @PutMapping("/{id}/result")
    public ResponseEntity<Match> updateMatchResult(
            @PathVariable Long id,
            @Valid @RequestBody MatchResultRequest request) {


        Match updatedMatch = matchService.updateMatchResult(id, request.getHomeScore(), request.getAwayScore());
        return ResponseEntity.ok(updatedMatch);
    }
    @GetMapping
    public ResponseEntity<Page<Match>> getAllMatches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Match> matches = matchService.getMatchesPaged(page, size);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable Long id) {
        Match match = matchService.getMatchById(id);
        return ResponseEntity.ok(match);
    }


}