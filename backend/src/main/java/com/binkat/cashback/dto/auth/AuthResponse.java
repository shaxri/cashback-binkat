package com.binkat.cashback.dto.auth;

import com.binkat.cashback.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record AuthResponse(
    @JsonProperty("access_token")
    String accessToken,
    
    @JsonProperty("refresh_token")
    String refreshToken,
    
    @JsonProperty("token_type")
    String tokenType,
    
    @JsonProperty("expires_in")
    long expiresIn,
    
    UserInfo user
) {
    public record UserInfo(
        Long id,
        String name,
        String email,
        String phone,
        UserRole role,
        String locale,
        Instant createdAt
    ) {}
}