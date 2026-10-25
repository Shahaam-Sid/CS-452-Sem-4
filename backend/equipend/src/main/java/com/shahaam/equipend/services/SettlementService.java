package com.shahaam.equipend.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shahaam.equipend.auth.User;
import com.shahaam.equipend.auth.UserRepository;
import com.shahaam.equipend.dto.SettlementRequestDTO;
import com.shahaam.equipend.dto.SettlementResponseDTO;
import com.shahaam.equipend.exceptions.AmountMismatchExpection;
import com.shahaam.equipend.exceptions.InvalidAmountException;
import com.shahaam.equipend.exceptions.MemberNotInGroupException;
import com.shahaam.equipend.exceptions.RecordNotFoundException;
import com.shahaam.equipend.exceptions.UserConflictException;
import com.shahaam.equipend.interfaces.ServicesInterface;
import com.shahaam.equipend.models.Group;
import com.shahaam.equipend.models.Settlement;
import com.shahaam.equipend.repositories.ExpenseSplitsRepository;
import com.shahaam.equipend.repositories.GroupMembersRepository;
import com.shahaam.equipend.repositories.GroupsRepository;
import com.shahaam.equipend.repositories.SettlementsRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class SettlementService implements ServicesInterface<SettlementResponseDTO, SettlementRequestDTO, Void> {
    private final SettlementsRepository settlementRepo;
    private final UserRepository userRepo;
    private final GroupsRepository groupRepo;
    private final GroupMembersRepository grpMemRepo;
    private final ExpenseSplitsRepository expSpltRepo;

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

        if (req.paidBy().equals(req.paidTo())) throw new UserConflictException();

        if (req.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidAmountException("Settlement Amount cannot be <= 0");

        User paidBy = userRepo.findById(req.paidBy())
            .orElseThrow(() -> new RecordNotFoundException("User", req.paidBy().toString()));

        User paidTo = userRepo.findById(req.paidTo())
            .orElseThrow(() -> new RecordNotFoundException("User", req.paidTo().toString()));

        Group group = groupRepo.findById(req.group())
            .orElseThrow(() -> new RecordNotFoundException("Group", req.group().toString()));

        if (!grpMemRepo.existsByGroup_GroupIDAndUser_UserID(req.group(), req.paidBy()))
            throw new MemberNotInGroupException(req.paidBy().toString(), req.group().toString());

        if (!grpMemRepo.existsByGroup_GroupIDAndUser_UserID(req.group(), req.paidTo()))
            throw new MemberNotInGroupException(req.paidTo().toString(), req.group().toString());

        UUID g = req.group();

        BigDecimal owed = expSpltRepo.allRecordedSplitsBetween(g, req.paidTo(), req.paidBy());
        BigDecimal settled = settlementRepo.allRecordedSettlementBetween(g, req.paidBy(), req.paidTo());

        BigDecimal reverseOwed = expSpltRepo.allRecordedSplitsBetween(g, req.paidBy(), req.paidTo());
        BigDecimal reverseSettled = settlementRepo.allRecordedSettlementBetween(g, req.paidTo(), req.paidBy());

        BigDecimal outstanding = owed.subtract(settled)
            .subtract(reverseOwed.subtract(reverseSettled));

        if (req.amount().compareTo(outstanding) > 0)
            throw new InvalidAmountException(
                "Amount exceeds outstanding balance of " + outstanding.max(BigDecimal.ZERO));

        Settlement settlement = new Settlement(req.note(), req.amount(), paidTo, paidBy, group);
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
        return settlementRepo.findByPaidTo_UserID(userID).stream()
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


// !! Direction. Verify that the splits query sums what paidBy owes paidTo, meaning splits where the split’s user is the settlement payer and the expense’s payer is the settlement payee. Reversing it would be a silent logic error.
// !! Concurrency. Two simultaneous settlements can both pass the overpayment check. Low priority here, but worth a note in your report.