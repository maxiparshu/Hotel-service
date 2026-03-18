package by.beltam.practice.dto;

import java.util.UUID;

public record AuthResponse(
        String accessToken,
        String tokenType,
        UUID id
) {}