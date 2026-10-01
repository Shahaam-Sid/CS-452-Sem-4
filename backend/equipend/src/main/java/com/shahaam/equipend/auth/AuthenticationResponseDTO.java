package com.shahaam.equipend.auth;

import java.util.UUID;

public record AuthenticationResponseDTO(
    UUID userID,
    String username,
    String fullName,
    String email,
    String token
) {}