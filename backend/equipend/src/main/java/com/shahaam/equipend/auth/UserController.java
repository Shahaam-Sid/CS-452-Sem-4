package com.shahaam.equipend.auth;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor 
@RestController 
@RequestMapping("/api/v1/user")
@Validated 
public class UserController {
    private final AuthService as;

    @GetMapping("/search")
    public ResponseEntity<List<SearchResultsDTO>> lookUpSearch(@RequestParam @NotBlank String query) {
        List<SearchResultsDTO> results = as.lookUpSearch(query);
        return ResponseEntity.ok(results);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SearchResultsDTO> searchById(
        @PathVariable 
        UUID id
    ) {
        return ResponseEntity.ok(as.searchById(id));
    }
    
}