package com.shahaam.equipend.auth;

import java.util.UUID;

public record SearchResultsDTO (
    UUID userId,
    String username,
    String fullName   
) {}