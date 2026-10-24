package com.shahaam.equipend.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shahaam.equipend.dto.SettlementRequestDTO;
import com.shahaam.equipend.dto.SettlementResponseDTO;
import com.shahaam.equipend.services.SettlementService;

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
@RequestMapping("api/v1/settlement")
@Validated 
public class SettlementController {
    private final SettlementService ss;

    @GetMapping("/{id}")
    public ResponseEntity<SettlementResponseDTO> getSettlement(
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        String id
    ) {
        return ResponseEntity.ok(ss.get(UUID.fromString(id)));
    }
    @GetMapping("/count-by-group/{id}")
    public ResponseEntity<Long> countGroupSettlementd(
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        String id
    ) {
        return ResponseEntity.ok(ss.countGroupSettlements(UUID.fromString(id)));
    }
    @GetMapping("/group/{id}")
    public ResponseEntity<List<SettlementResponseDTO>> getGroupSettlementd(
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        String id
    ) {
        return ResponseEntity.ok(ss.getGroupSettlements(UUID.fromString(id)));
    }
    @GetMapping("payee/{id}")
    public ResponseEntity<List<SettlementResponseDTO>> getByPayee(
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        String id
    ) {
        return ResponseEntity.ok(ss.getByPaidTo(UUID.fromString(id)));
    }
    @GetMapping("/payer/{id}")
    public ResponseEntity<List<SettlementResponseDTO>> getByPayer(
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        String id
    ) {
        return ResponseEntity.ok(ss.getByPayer(UUID.fromString(id)));
    }
    @GetMapping("/group/{groupId}/payee/{userId}")
    public ResponseEntity<List<SettlementResponseDTO>> getByPayee(
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        String groupId,
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        String userId
    ) {
        return ResponseEntity.ok(ss.getByGroupPaidTo(
            UUID.fromString(groupId),
            UUID.fromString(userId)));
    }
    @GetMapping("/group/{groupId}/payer/{userId}")
    public ResponseEntity<List<SettlementResponseDTO>> getByPayer(
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        String groupId,
        @PathVariable @NotBlank @Size(min = 36, max = 36, message = "Invalid UUID")
        String userId
    ) {
        return ResponseEntity.ok(ss.getByGroupPaidBy(
            UUID.fromString(groupId),
            UUID.fromString(userId)));
    }
    @PostMapping
    public ResponseEntity<SettlementResponseDTO> createSettlement(
        @RequestBody @Valid SettlementRequestDTO req
    ) {

        SettlementResponseDTO res = ss.add(req);
        String id = res.settlementID().toString();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(id).toUri();
        
        return  ResponseEntity.created(location).body(res);
    }
       
}