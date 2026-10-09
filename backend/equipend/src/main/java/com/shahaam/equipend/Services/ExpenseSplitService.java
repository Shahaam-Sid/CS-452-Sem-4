package com.shahaam.equipend.Services;

import org.springframework.stereotype.Service;

import com.shahaam.equipend.repositories.ExpenseSplitsRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ExpenseSplitService {
    private final ExpenseSplitsRepository expSplitRepo;
}
