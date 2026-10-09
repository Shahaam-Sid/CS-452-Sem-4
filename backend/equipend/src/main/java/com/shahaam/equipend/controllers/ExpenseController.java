package com.shahaam.equipend.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shahaam.equipend.Services.ExpenseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@RestController 
@RequestMapping("api/v1/expense")
@Validated 
public class ExpenseController {
    private final ExpenseService es;
}