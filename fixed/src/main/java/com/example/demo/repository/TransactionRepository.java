package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    Page<TransactionEntity> findByFromUserIdOrToUserId(
            Long fromUserId,
            Long toUserId,
            Pageable pageable
    );
}
