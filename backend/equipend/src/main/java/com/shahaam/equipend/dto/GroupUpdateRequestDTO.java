package com.shahaam.equipend.dto;

import jakarta.validation.constraints.Size;

public record GroupUpdateRequestDTO(

    @Size(min = 1, max = 50, message = "Group Name must be 1 - 50 characters long")
    String name,

    @Size(min = 0, max = 200, message = "Group Description can be 200 characters long")
    String description
) {}
