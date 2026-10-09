package com.shahaam.equipend.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SettlementRequestDTO(

    @NotBlank(message = "Note cannot be blank")
    @Size(min = 1, max = 255, message = "Note must be 1 - 255 characters long")
    String note,

    @NotNull(message = "Amount cannot be null")
    BigDecimal amount,

    @NotNull(message = "Paid to cannot be null")
    UUID paidTo,

    @NotNull(message = "Paid By cannot be null")
    UUID paidBy,

    @NotNull(message = "Group cannot be null")
    UUID group
) {}