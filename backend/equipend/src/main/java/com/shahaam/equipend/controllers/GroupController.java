package com.shahaam.equipend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shahaam.equipend.Services.GroupService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@RestController 
@RequestMapping("api/v1/group")
public class GroupController {
    private final GroupService gs;
}