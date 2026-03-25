package com.example.demo.service;

import java.math.BigDecimal;

public interface WalletService {
    Long createWallet(Long userId, String currency);
    BigDecimal getBalance(Long userId);
   
    BigDecimal deposit(Long userId, BigDecimal amount);
    void transfer(Long fromUserId, Long toUserId, BigDecimal amount);
}
