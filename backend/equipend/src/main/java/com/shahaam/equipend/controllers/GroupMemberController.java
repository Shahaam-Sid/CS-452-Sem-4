package com.shahaam.equipend.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shahaam.equipend.services.GroupMemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@RestController 
@RequestMapping("api/v1/member")
@Validated 
public class GroupMemberController {
    private final GroupMemberService gms;
}
