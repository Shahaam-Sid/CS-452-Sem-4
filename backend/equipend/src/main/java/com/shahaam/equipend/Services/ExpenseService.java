package com.shahaam.equipend.Services;

import org.springframework.stereotype.Service;

import com.shahaam.equipend.repositories.ExpensesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class ExpenseService {
    private final ExpensesRepository expenseRepo;
}
