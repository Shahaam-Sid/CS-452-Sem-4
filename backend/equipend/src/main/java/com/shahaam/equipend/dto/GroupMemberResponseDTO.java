package com.shahaam.equipend.dto;

import java.time.Instant;
import java.util.UUID;

public record GroupMemberResponseDTO(
    UUID user,

    UUID group,

    Instant joinedAt
) {}