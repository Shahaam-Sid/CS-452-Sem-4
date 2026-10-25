package com.shahaam.equipend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shahaam.equipend.dto.GroupRequestDTO;
import com.shahaam.equipend.dto.GroupResponseDTO;
import com.shahaam.equipend.services.GroupService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequiredArgsConstructor 
@RestController 
@RequestMapping("api/v1/group")
@Validated 
public class GroupController {
    private final GroupService gs;

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponseDTO> getGroup(
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        UUID id
    ) {
        return ResponseEntity.ok(gs.get(id));
    }
    @PostMapping
    public ResponseEntity<GroupResponseDTO> addGroup(
        @RequestBody @Valid GroupRequestDTO req
    ) {
        
        GroupResponseDTO res = gs.add(req);

        String id = res.groupID().toString();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        
        return ResponseEntity.created(location).body(res);
    }
}