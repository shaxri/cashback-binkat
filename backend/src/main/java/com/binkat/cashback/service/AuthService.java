package com.binkat.cashback.service;

import com.binkat.cashback.dto.auth.AuthResponse;
import com.binkat.cashback.dto.auth.LoginRequest;
import com.binkat.cashback.dto.auth.RegisterRequest;
import com.binkat.cashback.dto.auth.RefreshTokenRequest;
import com.binkat.cashback.enums.UserRole;
import com.binkat.cashback.model.RefreshToken;
import com.binkat.cashback.model.User;
import com.binkat.cashback.model.Wallet;
import com.binkat.cashback.repository.RefreshTokenRepository;
import com.binkat.cashback.repository.UserRepository;
import com.binkat.cashback.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class AuthService {
    
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    @Value("${spring.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;
    
    public AuthService(UserRepository userRepository,
                      WalletRepository walletRepository,
                      RefreshTokenRepository refreshTokenRepository,
                      PasswordEncoder passwordEncoder,
                      JwtService jwtService) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.email()));
        
        if (!user.getActive()) {
            throw new BadCredentialsException("Account is deactivated");
        }
        
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        
        // Revoke existing refresh tokens
        refreshTokenRepository.revokeAllByUser(user);
        
        return generateAuthResponse(user);
    }
    
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        if (request.phone() != null && userRepository.existsByPhone(request.phone())) {
            throw new IllegalArgumentException("Phone number already registered");
        }
        
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setLocale(request.locale() != null ? request.locale() : "uz-Latn");
        user.setRole(UserRole.CUSTOMER);
        user.setActive(true);
        
        user = userRepository.save(user);
        
        // Create wallet for new user
        Wallet wallet = new Wallet(user);
        walletRepository.save(wallet);
        
        return generateAuthResponse(user);
    }
    
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
            .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
        
        if (!refreshToken.isValid()) {
            throw new BadCredentialsException("Refresh token is expired or revoked");
        }
        
        User user = refreshToken.getUser();
        
        // Revoke the used refresh token
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
        
        return generateAuthResponse(user);
    }
    
    private AuthResponse generateAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        long expiresIn = jwtService.getAccessTokenExpiration();
        
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getRole(),
            user.getLocale(),
            user.getCreatedAt()
        );
        
        return new AuthResponse(accessToken, refreshToken, "Bearer", expiresIn, userInfo);
    }
    
    private String generateRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusMillis(refreshTokenExpiration);
        
        RefreshToken refreshToken = new RefreshToken(token, user, expiresAt);
        refreshTokenRepository.save(refreshToken);
        
        return token;
    }
}