package com.shahaam.equipend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.shahaam.equipend.enums.SplitType;

public record ExpenseResponseDTO(
    UUID expenseID,
    String title,
    BigDecimal amount,
    UUID groupID,
    UUID createdBy,
    UUID paidBy,
    Instant createdAt,
    SplitType splitType
) {}