package com.shahaam.equipend.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticateRequestDTO(

    @Size(min = 3, max = 128, message = "Invalid Identifier: must contain 3 - 128 characters")
    String identifier,
    
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 128, message = "Password cannot must contain 3 - 128")
    String password
) {}

//Either email or username can be sent with password