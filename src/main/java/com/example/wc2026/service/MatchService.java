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
        // Rule 1: Chặn nếu đội nhà trùng đội khách
        if (request.getHomeTeamId().equals(request.getAwayTeamId())) {
            throw new RuntimeException("Một đội bóng không thể tự thi đấu với chính mình!");
        }

        // Rule 2: Kiểm tra Đội nhà có tồn tại trong hệ thống chưa
        Team homeTeam = teamRepository.findById(request.getHomeTeamId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Đội nhà với ID: " + request.getHomeTeamId()));

        // Rule 3: Kiểm tra Đội khách có tồn tại trong hệ thống chưa
        Team awayTeam = teamRepository.findById(request.getAwayTeamId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Đội khách với ID: " + request.getAwayTeamId()));

        // 🌟 TẠO MÃ TRẬN NGẪU NHIÊN 6 KÝ TỰ (Ví dụ: 8A2F9B)
        String randomMatchCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        // Thiết lập thực thể và lưu trữ
        Match match = Match.builder()
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .startTime(request.getStartTime())
                .homeScore(0)
                .awayScore(0)
                .status("PENDING") // Mặc định là chưa diễn ra
                .matchCode(randomMatchCode) // 🌟 Cấy mã ngẫu nhiên vào đây
                .build();

        return matchRepository.save(match);
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    // API mới cho Admin cập nhật tỷ số
    @Transactional
    public Match updateMatchResult(Long matchId, Integer homeScore, Integer awayScore) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trận đấu!"));

        if ("FINISHED".equals(match.getStatus())) {
            throw new RuntimeException("Trận đấu này đã kết thúc và được trả thưởng rồi!");
        }

        // Cập nhật tỷ số và chốt trạng thái trận đấu
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        match.setStatus("FINISHED");
        Match savedMatch = matchRepository.save(match);

        // Kích hoạt chuỗi dây chuyền: Gọi sang BetTicketService để quét vé cược và phát thưởng
        betTicketService.resolveMatchBets(savedMatch);

        return savedMatch;
    }

    public Page<Match> getMatchesPaged(int page, int size) {
        // Sắp xếp các trận đấu mới nhất lên đầu dựa vào id giảm dần
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return matchRepository.findAll(pageable);
    }

    public Match getMatchById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trận đấu với ID: " + id));
    }
}