package com.shahaam.equipend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record SettlementResponseDTO(
    UUID settlementID,
    String note,
    BigDecimal amount,
    Instant timestamp,
    UUID paidTo,
    UUID paidBy,
    UUID group
) {}