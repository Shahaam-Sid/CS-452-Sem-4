package com.shahaam.equipend.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GroupRequestDTO(
    
    @NotBlank(message = "Name cannot be blank") 
    @Size(min = 1, max = 50, message = "Group Name must be 1 - 50 characters long")
    String name,

    @Size(min = 0, max = 200, message = "Group Name can be 200 characters long")
    String description,

    @NotBlank(message = "Currency cannot be blank")
    @Size(min = 1, max = 10, message = "Currency must be 1 - 10 characters long")
    String currency,

    @NotNull(message = "Created By cannot be Null")
    UUID createdBy
) {}