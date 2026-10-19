package com.shahaam.equipend.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.shahaam.equipend.enums.SplitType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExpenseRequestDTO(

    @NotBlank(message = "Title cannot be blank")
    String title,

    @NotNull(message = "Amount cannot be Null")
    BigDecimal amount,

    @NotNull(message = "Group ID cannot be Null")
    UUID groupID,

    @NotNull(message = "Created By cannot be Null")
    UUID createdBy,

    @NotNull(message = "Paid By cannot be Null")
    UUID paidBy,

    @NotNull(message = "Split Type cannot be Null")
    SplitType splitType,

    List<UUID> participants,

    Map<UUID, BigDecimal> participantShare
    
) {}