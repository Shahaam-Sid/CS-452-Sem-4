package com.shahaam.equipend.Services;

import org.springframework.stereotype.Service;

import com.shahaam.equipend.repositories.SettlementsRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class SettlementService {
    private final SettlementsRepository settlementRepo;
}
