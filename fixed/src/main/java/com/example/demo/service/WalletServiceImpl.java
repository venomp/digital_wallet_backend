package com.example.demo.service;

import java.math.BigDecimal;
import java.util.Optional;

import com.example.demo.exception.InsufficientBalanceException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.TransactionStatus;
import org.springframework.stereotype.Service;

import com.example.demo.model.TransactionEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.model.WalletEntity;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WalletRepository;

import jakarta.transaction.Transactional;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public WalletServiceImpl(WalletRepository walletRepository,
                             UserRepository userRepository,
                             TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public Long createWallet(Long userId, String currency) {

       
        Optional<WalletEntity> existing = walletRepository.findByUserId(userId);
        if (existing.isPresent()) {
            throw new ResourceAlreadyExistsException("Wallet already exists for this user");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        WalletEntity wallet = new WalletEntity();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCurrency(currency.toUpperCase()); 
        wallet.setUser(user);

        walletRepository.save(wallet);
        return wallet.getId();
    }

    @Override
    public BigDecimal getBalance(Long userId) {
        WalletEntity wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        return wallet.getBalance();
    }

    @Override
    @Transactional
   
    public BigDecimal deposit(Long userId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        WalletEntity wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
        return wallet.getBalance();
    }

    @Override
    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Cannot transfer to the same user");
        }

        TransactionEntity log = new TransactionEntity();
        log.setAmount(amount);
        log.setFromUserId(fromUserId);
        log.setToUserId(toUserId);
        log.setStatus(TransactionStatus.PENDING);
        

        try {
           
            Long firstId = fromUserId < toUserId ? fromUserId : toUserId;
            Long secondId = fromUserId < toUserId ? toUserId : fromUserId;

            WalletEntity first = walletRepository.findByUserIdForUpdate(firstId)
                    .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user " + firstId));
            WalletEntity second = walletRepository.findByUserIdForUpdate(secondId)
                    .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user " + secondId));

            WalletEntity from = first.getUser().getId().equals(fromUserId) ? first : second;
            WalletEntity to   = first.getUser().getId().equals(fromUserId) ? second : first;

           
            if (from.getBalance().compareTo(amount) < 0) {
                throw new InsufficientBalanceException("Insufficient balance");
            }

            from.setBalance(from.getBalance().subtract(amount));
            to.setBalance(to.getBalance().add(amount));

            log.setStatus(TransactionStatus.SUCCESS);
            transactionRepository.save(log);

        } catch (Exception e) {
            log.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(log);
            throw e;
        }
    }
}
