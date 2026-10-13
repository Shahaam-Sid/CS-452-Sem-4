package com.shahaam.equipend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shahaam.equipend.auth.User;
import com.shahaam.equipend.auth.UserRepository;
import com.shahaam.equipend.dto.SettlementRequestDTO;
import com.shahaam.equipend.dto.SettlementResponseDTO;
import com.shahaam.equipend.exceptions.RecordNotFoundException;
import com.shahaam.equipend.interfaces.ServicesInterface;
import com.shahaam.equipend.models.Group;
import com.shahaam.equipend.models.Settlement;
import com.shahaam.equipend.repositories.GroupsRepository;
import com.shahaam.equipend.repositories.SettlementsRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class SettlementService implements ServicesInterface<SettlementResponseDTO, SettlementRequestDTO, Void> {
    private final SettlementsRepository settlementRepo;
    private final UserRepository userRepo;
    private final GroupsRepository groupRepo;

    @Override
    @Transactional(readOnly = true)
    public SettlementResponseDTO get(UUID settlementId) {

        Settlement settlement = settlementRepo.findById(settlementId)
        .orElseThrow(() -> new RecordNotFoundException("Settlement", settlementId.toString()));

        return mapToSettlementResponseDTO(settlement);
    }

    @Override
    @Transactional
    public SettlementResponseDTO add(SettlementRequestDTO req) {
        if (!userRepo.existsById(req.paidTo()))
            throw new RecordNotFoundException("User", req.paidTo().toString());
    
        if (!userRepo.existsById(req.paidBy()))
            throw new RecordNotFoundException("User", req.paidBy().toString());

        if (!groupRepo.existsById(req.group()))
            throw new RecordNotFoundException("Group", req.group().toString());

        Settlement settlement = mapFromSettlementRequestDTO(req);

        settlementRepo.save(settlement);

        return mapToSettlementResponseDTO(settlement);
    }

    @Transactional(readOnly = true)
    public Long countGroupSettlements(UUID groupID) {
        return settlementRepo.countByGroup_GroupID(groupID);
    }

    @Transactional(readOnly = true)
    public List<SettlementResponseDTO> getGroupSettlements(UUID groupID) {
        return settlementRepo.findByGroup_GroupID(groupID)
        .stream().map(this::mapToSettlementResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<SettlementResponseDTO> getByPaidTo(UUID userID) {
        return settlementRepo.findByPaidto_UserID(userID).stream()
        .map(this::mapToSettlementResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<SettlementResponseDTO> getByPayer(UUID userID) {
        return settlementRepo.findByPaidBy_UserID(userID).stream()
        .map(this::mapToSettlementResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<SettlementResponseDTO> getByGroupPaidTo(UUID groupID, UUID userID) {
        return settlementRepo.findByGroup_GroupIDAndPaidTo_UserID(groupID, userID)
        .stream().map(this::mapToSettlementResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<SettlementResponseDTO> getByGroupPaidBy(UUID groupID, UUID userID) {
        return settlementRepo.findByGroup_GroupIDAndPaidBy_UserID(groupID, userID)
        .stream().map(this::mapToSettlementResponseDTO).toList();
    }

    protected Settlement mapFromSettlementRequestDTO(SettlementRequestDTO req) {

        User paidTo = userRepo.findById(req.paidTo())
        .orElseThrow(() -> new RecordNotFoundException("User", req.paidTo().toString()));

        User paidBy = userRepo.findById(req.paidBy())
        .orElseThrow(() -> new RecordNotFoundException("User", req.paidBy().toString()));

        Group group = groupRepo.findById(req.group())
        .orElseThrow(() -> new RecordNotFoundException("Group", req.group().toString()));

        return new Settlement(req.note(), req.amount(), paidTo, paidBy, group);
    }

    protected SettlementResponseDTO mapToSettlementResponseDTO(Settlement settlement) {

        return new SettlementResponseDTO(
            settlement.getSettlementID(), settlement.getNote(), settlement.getAmount(),
            settlement.getTimestamp(), settlement.getPaidTo().getUserID(), settlement.getPaidBy().getUserID(),
            settlement.getGroup().getGroupID()
        );
    }
}
