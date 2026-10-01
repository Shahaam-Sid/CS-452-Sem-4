package com.shahaam.equipend.auth;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 3, max = 100, message="Name must contain 3 - 100 characters")
    String fullName,

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must contain 3 - 50 characters")
    String username,

    @NotBlank(message = "Email cannot be empty")
    @Size(min = 5, max = 128, message = "Email must contain 3 - 128 characters")
    String email,

    @NotNull(message = "Date of Birth cannot be null")
    LocalDate dob,

    @NotBlank(message = "Country cannot be empty")
    @Size(min = 3, max = 50, message = "Country must contain 3 - 50 characters")
    String country,

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 128, message = "Password cannot must contain 3 - 128")
    String password
) {}