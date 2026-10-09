package com.shahaam.equipend.Services;

import org.springframework.stereotype.Service;

import com.shahaam.equipend.repositories.GroupMembersRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class GroupMemberService {
    private final GroupMembersRepository grpMemberRepo;
}
