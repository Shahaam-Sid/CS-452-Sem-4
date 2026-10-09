package com.shahaam.equipend.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shahaam.equipend.Services.ExpenseSplitService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@RestController 
@RequestMapping("/api/v1/splits")
@Validated 
public class ExpenseSplitController {
    private final ExpenseSplitService ess;
}