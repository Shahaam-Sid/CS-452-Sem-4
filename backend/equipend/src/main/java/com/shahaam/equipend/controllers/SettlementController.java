package com.shahaam.equipend.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shahaam.equipend.services.SettlementService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@RestController
@RequestMapping("api/v1/settlement")
@Validated 
public class SettlementController {
    private final SettlementService ss;
}
