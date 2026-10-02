package com.shahaam.equipend.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor  
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(
        @RequestBody @Valid RegisterRequestDTO req
    ) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
        @RequestBody @Valid AuthenticateRequestDTO req
    ) {        
        return ResponseEntity.ok(authService.authenticate(req));
    }   
}