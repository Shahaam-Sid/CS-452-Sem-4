package com.shahaam.equipend.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.shahaam.equipend.enums.SplitType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ExpenseRequestDTO(

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 5, max = 100, message = "Title can be 5 - 100 characters")
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

    @NotNull(message = "Participants cannot be Null")
    List<UUID> participants,

    Map<UUID, BigDecimal> participantShare
    
) {}