package com.shahaam.equipend.Services;

import org.springframework.stereotype.Service;

import com.shahaam.equipend.repositories.GroupsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class GroupService {
    private final GroupsRepository groupRepo;
}
