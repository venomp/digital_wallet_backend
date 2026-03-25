package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final WalletService walletService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder encoder,
                           JwtService jwtService,
                           WalletService walletService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtService = jwtService;
        
        this.walletService = walletService;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(request.getEmail());
        userEntity.setPhoneNumber(request.getPhoneNumber());
        
        userEntity.setFullName(request.getFullName());
        userEntity.setPassword(encoder.encode(request.getPassword()));

        UserEntity saved = userRepository.save(userEntity);

    
        walletService.createWallet(saved.getId(), "INR");

        AuthResponse response = new AuthResponse();
        response.setUserId(saved.getId());
        response.setMessage("User registered successfully");
        return response;
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        AuthResponse response = new AuthResponse();

       
        Optional<UserEntity> userOpt = userRepository.findByEmail(request.getEmail());

        
        if (userOpt.isEmpty() || !encoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            response.setMessage("Invalid email or password");
            return response;
        }

        UserEntity user = userOpt.get();
        response.setMessage("Login successful");
        response.setToken(jwtService.generateToken(user.getId()));
        response.setType("Bearer");
        return response;
    }
}
