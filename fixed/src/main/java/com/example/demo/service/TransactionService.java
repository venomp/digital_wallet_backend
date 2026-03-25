package com.example.demo.service;

import org.springframework.data.domain.Page;
import com.example.demo.dto.TransactionResponse;

public interface TransactionService {
    Page<TransactionResponse> getUserTransactions(Long userId, int page, int size);
}
