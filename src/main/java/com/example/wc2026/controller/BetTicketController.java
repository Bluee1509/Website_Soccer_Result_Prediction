package com.example.wc2026.controller;

import com.example.wc2026.dto.BetTicketRequest;
import com.example.wc2026.model.BetTicket;
import com.example.wc2026.service.BetTicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/bets")
public class BetTicketController {

    @Autowired
    private BetTicketService betTicketService;

    @PostMapping
    public ResponseEntity<BetTicket> placeBet(
            @Valid @RequestBody BetTicketRequest request,
            Principal principal) {   // ← THÊM Principal vào đây

        if (principal == null) {
            throw new RuntimeException("Vui lòng đăng nhập để đặt cược!");
        }

        String username = principal.getName();

        BetTicket newTicket = betTicketService.placeBet(request, username);
        return ResponseEntity.ok(newTicket);
    }

    @GetMapping("/my-tickets")
    public ResponseEntity<List<BetTicket>> getMyTickets(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Vui lòng đăng nhập!");
        }
        String username = principal.getName();
        List<BetTicket> myTickets = betTicketService.getMyTickets(username);
        return ResponseEntity.ok(myTickets);
    }
}