package com.example.wc2026.service;

import com.example.wc2026.dto.TeamRequest;
import com.example.wc2026.model.Team;
import com.example.wc2026.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;


    public Team createTeam(TeamRequest request) {
        String teamName = request.getName().trim();

        if (teamRepository.existsByName(teamName)) {
            throw new RuntimeException("Đội bóng này đã tồn tại trong hệ thống!");
        }

        Team team = Team.builder()
                .name(teamName)
                .logoUrl(request.getLogoUrl())
                .build();

        return teamRepository.save(team);
    }


    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
}