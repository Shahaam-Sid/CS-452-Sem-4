package com.shahaam.equipend.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shahaam.equipend.dto.ExpenseSplitRequestDTO;
import com.shahaam.equipend.dto.ExpenseSplitResponseDTO;
import com.shahaam.equipend.services.ExpenseSplitService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RequiredArgsConstructor 
@RestController 
@RequestMapping("/api/v1/splits")
@Validated 
public class ExpenseSplitController {
    private final ExpenseSplitService ess;

    @GetMapping({"expense/{expenseId}/user/{userId}"})
    public ResponseEntity<ExpenseSplitResponseDTO> getSplit(
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        String expenseId,
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        String userId
    ) {
        return ResponseEntity.ok(ess.get(UUID.fromString(expenseId), UUID.fromString(userId)));
    }
    @GetMapping("/expense/{id}")
    public ResponseEntity<List<ExpenseSplitResponseDTO>> getSplitByExpense(
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        String id
    ) {
        return ResponseEntity.ok(ess.getByExpense(UUID.fromString(id)));
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<List<ExpenseSplitResponseDTO>> getSplitByUser(
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        String id
    ) {
        return ResponseEntity.ok(ess.getByUser(UUID.fromString(id)));
    }

    @PostMapping
    public ResponseEntity<ExpenseSplitResponseDTO> createSplit(
        @RequestBody @Valid ExpenseSplitRequestDTO req) {
        
        ExpenseSplitResponseDTO res = ess.add(req);
        String expenseId = res.expense().toString();
        String userId = res.user().toString();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("expense/{expenseId}/user/{userId}")
        .buildAndExpand(expenseId, userId).toUri();
        return ResponseEntity.created(location).body(res);
    }
}