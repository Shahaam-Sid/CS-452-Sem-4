package com.shahaam.equipend.dto;

import java.time.Instant;
import java.util.UUID;

public record GroupResponseDTO(

    UUID groupID,

    String name,

    String description,

    String currency,

    String createdBy,

    Instant createdAt
) {}