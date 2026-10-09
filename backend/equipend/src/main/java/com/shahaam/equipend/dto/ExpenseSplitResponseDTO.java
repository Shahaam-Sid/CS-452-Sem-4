package com.shahaam.equipend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ExpenseSplitResponseDTO(
    UUID user,

    UUID expense,

    BigDecimal shareAmount
) {}