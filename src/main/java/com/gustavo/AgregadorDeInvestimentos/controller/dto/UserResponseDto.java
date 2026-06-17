package com.gustavo.AgregadorDeInvestimentos.controller.dto;

import com.gustavo.AgregadorDeInvestimentos.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDto(
        UUID userId,
        String username,
        String email,
        Instant createdAt
) {
    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreationTimeStamp()
        );
    }
}
