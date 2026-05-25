package com.example.wc2026.service;

import com.example.wc2026.model.Transaction;
import com.example.wc2026.model.User;
import com.example.wc2026.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public void logTransaction(User user, String type, BigDecimal amount) {
        Transaction tx = Transaction.builder()
                .user(user)
                .type(type)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .build();
        transactionRepository.save(tx);
    }
    public List<Transaction> getMyTransactions(String username) {
        return transactionRepository.findByUserUsernameOrderByCreatedAtDesc(username);
    }
}