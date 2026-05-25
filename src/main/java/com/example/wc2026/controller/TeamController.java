package com.example.wc2026.controller;

import com.example.wc2026.dto.TeamRequest;
import com.example.wc2026.model.Team;
import com.example.wc2026.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping
    public ResponseEntity<Team> addTeam(@Valid @RequestBody TeamRequest request) {
        Team newTeam = teamService.createTeam(request);
        return ResponseEntity.ok(newTeam);
    }

    @GetMapping
    public ResponseEntity<List<Team>> getAll() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }
}