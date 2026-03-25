package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.TransactionResponse;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.model.TransactionEntity;
import com.example.demo.repository.TransactionRepository;


@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                   TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public Page<TransactionResponse> getUserTransactions(Long userId, int page, int size) {
        
        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TransactionEntity> transactions =
                transactionRepository.findByFromUserIdOrToUserId(userId, userId, pageable);

        return transactions.map(transactionMapper::toDto);
    }
}
