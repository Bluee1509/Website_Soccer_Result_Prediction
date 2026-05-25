package com.example.wc2026.controller;

import com.example.wc2026.model.Transaction;
import com.example.wc2026.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal; // Thêm thư viện này
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/my-transactions")
    public ResponseEntity<List<Transaction>> getMyTransactions(Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(transactionService.getMyTransactions(username));
    }
}