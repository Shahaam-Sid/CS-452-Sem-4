package com.shahaam.equipend.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record ExpenseSplitRequestDTO(

    @NotNull(message = "User cannot be Null")
    UUID user,

    @NotNull(message = "Expense cannot be Null")
    UUID expense,

    @NotNull(message = "Share Amount cannot be Null")
    BigDecimal shareAmount
) {}