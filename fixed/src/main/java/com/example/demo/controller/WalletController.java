package com.example.demo.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.DepositRequest;
import com.example.demo.dto.Transaction;
import com.example.demo.service.WalletService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance() {
        Long userId = getAuthenticatedUserId();
        return ResponseEntity.ok(walletService.getBalance(userId));
    }

    // FIX: endpoint was "/deposite" (typo) — corrected to "/deposit"
    @PostMapping("/deposit")
    public ResponseEntity<BigDecimal> deposit(@Valid @RequestBody DepositRequest deposit) {
        Long userId = getAuthenticatedUserId();
        // FIX: calling corrected "deposit" method (was "deposite")
        BigDecimal balance = walletService.deposit(userId, deposit.getAmount());
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@Valid @RequestBody Transaction transaction) {
        Long userId = getAuthenticatedUserId();
        walletService.transfer(userId, transaction.getToUserId(), transaction.getAmount());
        // FIX: fixed typo "sucessful" → "successful"
        return ResponseEntity.ok("Transfer successful");
    }

    // FIX: extracted helper to avoid duplicating principal extraction logic in every method
    // FIX: was using (long) cast in one endpoint and (Long) in another — now consistent
    private Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getPrincipal();
    }
}
