package com.shahaam.equipend.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shahaam.equipend.dto.GroupMemberRequestDTO;
import com.shahaam.equipend.dto.GroupMemberResponseDTO;
import com.shahaam.equipend.services.GroupMemberService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequiredArgsConstructor 
@RestController 
@RequestMapping("api/v1/member")
@Validated 
public class GroupMemberController {
    private final GroupMemberService gms;

    @GetMapping("/group/{groupId}/user/{userId}")
    public ResponseEntity<GroupMemberResponseDTO> getMember(
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        UUID groupId,
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        UUID userId
    ) {
        return ResponseEntity.ok(gms.get(groupId, userId));
    }
    @GetMapping("/group/{id}")
    public ResponseEntity<List<GroupMemberResponseDTO>> getByGroup(
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        UUID id
    ) {
        return ResponseEntity.ok(gms.getByGroup(id));
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<List<GroupMemberResponseDTO>> getByUser(
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        UUID id
    ) {
        return ResponseEntity.ok(gms.getByUser(id));
    }
    @PostMapping
    public ResponseEntity<GroupMemberResponseDTO> createMember(
        @RequestBody @Valid GroupMemberRequestDTO req    
    ) {
        GroupMemberResponseDTO res = gms.add(req);
        String groupId = res.group().toString();
        String userId = res.user().toString();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/group/{groupId}/user/{userId}")
        .buildAndExpand(groupId, userId).toUri();

        return ResponseEntity.created(location).body(res);
    }
}