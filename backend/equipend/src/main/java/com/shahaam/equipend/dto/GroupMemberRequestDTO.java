package com.shahaam.equipend.dto;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record GroupMemberRequestDTO(
    
    @NotNull(message = "User cannot be Null")
    UUID user,

    @NotNull(message = "Group cannot be Null")
    UUID group,

    @NotNull(message = "Joined At cannot be Null")
    Instant joinedAt
) {}