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
        // KHÔNG CẦN TRY-CATCH NỮA! Lỗi nếu có tự động bay thẳng vào GlobalExceptionHandler
        Match newMatch = matchService.createMatch(request);
        return ResponseEntity.ok(newMatch);
    }


    // API dành cho Admin nhập kết quả trận đấu và kích hoạt hệ thống trả thưởng
    @PutMapping("/{id}/result")
    public ResponseEntity<Match> updateMatchResult(
            @PathVariable Long id,
            @Valid @RequestBody MatchResultRequest request) {

        // Không cần try-catch, lỗi trận đấu đã kết thúc sẽ tự động nhảy vào GlobalExceptionHandler
        Match updatedMatch = matchService.updateMatchResult(id, request.getHomeScore(), request.getAwayScore());
        return ResponseEntity.ok(updatedMatch);
    }
    @GetMapping
    public ResponseEntity<Page<Match>> getAllMatches(
            @RequestParam(defaultValue = "0") int page,  // Số trang (mặc định trang 0)
            @RequestParam(defaultValue = "10") int size // Số phần tử mỗi trang (mặc định 10 dòng)
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