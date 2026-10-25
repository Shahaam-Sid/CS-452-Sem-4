package com.shahaam.equipend.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shahaam.equipend.dto.ExpenseRequestDTO;
import com.shahaam.equipend.dto.ExpenseResponseDTO;
import com.shahaam.equipend.services.ExpenseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequiredArgsConstructor 
@RestController 
@RequestMapping("/api/v1/expense")
@Validated 
public class ExpenseController {
    private final ExpenseService es;

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpense(
        @PathVariable 
        UUID id
    ) {
        return ResponseEntity.ok(es.get(id));
    }
    @GetMapping("/creator/{id}")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesByCreator(
        @PathVariable 
        UUID id
    ) {
        return ResponseEntity.ok(es.getExpensesByCreator(id));
    }
    @GetMapping("/payer/{id}")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesByPayer(
        @PathVariable 
        UUID id
    ) {
        return ResponseEntity.ok(es.getExpensesByPayer(id));
    }
    @GetMapping("/group/{id}")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesByGroup(
        @PathVariable 
        UUID id
    ) {
        return ResponseEntity.ok(es.getGroupExpenses(id));
    }
    @GetMapping("/count-by-group/{id}")
    public ResponseEntity<Long> getCountByGroup(
        @PathVariable 
        UUID id
    ) {
        return ResponseEntity.ok(es.countGroupExpenses(id));
    }
    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> addExpense(
        @RequestBody @Valid ExpenseRequestDTO req
    ) {
        
        ExpenseResponseDTO res = es.add(req);
        String id = res.expenseID().toString();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(id).toUri();
        
        return ResponseEntity.created(location).body(res);
    }
}