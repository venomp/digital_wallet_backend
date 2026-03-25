package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.TransactionResponse;
import com.example.demo.model.TransactionEntity;

@Component
public class TransactionMapper {

    public TransactionResponse toDto(TransactionEntity entity) {
        TransactionResponse response = new TransactionResponse();
        response.setFromUserId(entity.getFromUserId());
        response.setToUserId(entity.getToUserId());
        response.setAmount(entity.getAmount());
        response.setCreatedAt(entity.getCreatedAt());
        response.setStatus(entity.getStatus().name());
        return response;
    }
}
