package com.shahaam.equipend.services;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shahaam.equipend.auth.User;
import com.shahaam.equipend.auth.UserRepository;
import com.shahaam.equipend.dto.ExpenseRequestDTO;
import com.shahaam.equipend.dto.ExpenseResponseDTO;
import com.shahaam.equipend.enums.SplitType;
import com.shahaam.equipend.exceptions.AmountMismatchExpection;
import com.shahaam.equipend.exceptions.DomainNullException;
import com.shahaam.equipend.exceptions.InvalidAmountException;
import com.shahaam.equipend.exceptions.MemberNotInGroupException;
import com.shahaam.equipend.exceptions.RecordNotFoundException;
import com.shahaam.equipend.exceptions.UserConflictException;
import com.shahaam.equipend.interfaces.ServicesInterface;
import com.shahaam.equipend.models.Expense;
import com.shahaam.equipend.models.ExpenseSplit;
import com.shahaam.equipend.models.Group;
import com.shahaam.equipend.repositories.ExpenseSplitsRepository;
import com.shahaam.equipend.repositories.ExpensesRepository;
import com.shahaam.equipend.repositories.GroupMembersRepository;
import com.shahaam.equipend.repositories.GroupsRepository;
import com.shahaam.equipend.utils.UtilityMethods;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class ExpenseService implements ServicesInterface<ExpenseResponseDTO, ExpenseRequestDTO, Void> {
    
    private final ExpensesRepository expenseRepo;
    private final GroupsRepository groupsRepo;
    private final UserRepository userRepo;
    private final GroupMembersRepository grpMemRepo;
    private final ExpenseSplitsRepository expSplitRepo;

    @Transactional(readOnly = true)
    public long countGroupExpenses(UUID groupID) {
        return expenseRepo.countByGroup_GroupID(groupID);
    }

    @Override
    @Transactional
    public ExpenseResponseDTO add(ExpenseRequestDTO req) {

        if (!userRepo.existsById(req.createdBy()))
            throw new RecordNotFoundException("User", req.createdBy().toString());

        if (!userRepo.existsById(req.paidBy()))
            throw new RecordNotFoundException("User", req.paidBy().toString());
            
        if (!groupsRepo.existsById(req.groupID()))
            throw new RecordNotFoundException("Group", req.groupID().toString());

        if (!grpMemRepo.existsByGroup_GroupIDAndUser_UserID(req.groupID(), req.createdBy()))
            throw new MemberNotInGroupException(req.groupID().toString(), req.createdBy().toString());

        if (!grpMemRepo.existsByGroup_GroupIDAndUser_UserID(req.groupID(), req.createdBy()))
            throw new MemberNotInGroupException(req.groupID().toString(), req.paidBy().toString());

        if (req.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidAmountException("Expense Amount cannot be <= 0");

        // Check if the size of the Set (unique items) is smaller than the List
        Set<UUID> uniqueParticipants = new HashSet<>(req.participants());
        if (uniqueParticipants.size() < req.participants().size()) {
            throw new UserConflictException("Duplicate participant IDs are not allowed.");
}

        boolean allInGrp = req.participants().stream().allMatch(
            id -> grpMemRepo.existsByGroup_GroupIDAndUser_UserID(req.groupID(), id)
        );

        if (req.splitType() != SplitType.EQUAL) {

            if (req.participantShare() == null)
                throw new DomainNullException("Participant Share");

            boolean allPositive = req.participantShare().values().stream().allMatch(
                amount -> amount.compareTo(BigDecimal.ZERO) <= 0
            );
            if (!allPositive) throw new InvalidAmountException("Participant Amount cannot be <= 0");

            boolean allKeyInGrp = req.participantShare().keySet().stream().allMatch(
                id -> grpMemRepo.existsByGroup_GroupIDAndUser_UserID(req.groupID(), id)
            );

             if (!allKeyInGrp) throw new MemberNotInGroupException("Some", "this");
        }

        if (!allInGrp) throw new MemberNotInGroupException("Some", "this");

        Expense expense = mapFromExpenseRequestDTO(req);

        expenseRepo.save(expense);

        expenseRepo.flush();

        Map<UUID, BigDecimal> shares;

        switch (req.splitType()) {
            case EQUAL -> {
                shares = UtilityMethods.calculateEqualSplit(
                    req.amount(), req.paidBy(), req.participants()
                );
            }
            case PERCENTAGE -> {

                shares = UtilityMethods.calculatePercentageSplit(
                    req.amount(), req.participantShare()
                );
            } case AMOUNT -> {
                BigDecimal total = BigDecimal.ZERO;

                shares = req.participantShare();

                for (BigDecimal share : shares.values()) {
                    total = total.add(share);
                }

                if (total.compareTo(req.amount()) != 0)
                    throw new AmountMismatchExpection();
            }

            default -> throw new UnsupportedOperationException("Invalid Split type");
        }

        shares.remove(req.paidBy());

        shares.entrySet().stream().forEach(
            entry -> expSplitRepo.save(mapFromExpenseSplitRequest(
                entry.getKey(), expense.getExpenseID(), entry.getValue()
            ))
        );
        return mapToExpenseResponseDTO(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponseDTO get(UUID id) {
    
        Expense expense = expenseRepo.findById(id)
        .orElseThrow(() -> new RecordNotFoundException("Expense", id.toString()));

        return mapToExpenseResponseDTO(expense);
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponseDTO> getGroupExpenses(UUID groupID) {
        return expenseRepo.findByGroup_GroupID(groupID).stream()
        .map(this::mapToExpenseResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponseDTO> getExpensesByCreator(UUID creator) {
        return expenseRepo.findByCreatedBy_UserID(creator).stream()
        .map(this::mapToExpenseResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponseDTO> getExpensesByPayer(UUID payer) {
        return expenseRepo.findByPaidBy_UserID(payer).stream()
        .map(this::mapToExpenseResponseDTO).toList();
    }

    protected Expense mapFromExpenseRequestDTO(ExpenseRequestDTO req) {

        Group group = groupsRepo.findById(req.groupID())
        .orElseThrow(() -> new RecordNotFoundException("Group", req.groupID().toString()));

        User createdBy = userRepo.findById(req.createdBy())
        .orElseThrow(() -> new RecordNotFoundException("User", req.createdBy().toString()));

        User paidBy = userRepo.findById(req.paidBy())
        .orElseThrow(() -> new RecordNotFoundException("User", req.paidBy().toString()));

        return new Expense(req.title(),req.amount(), group, createdBy, paidBy, req.splitType());
    }

    protected ExpenseResponseDTO mapToExpenseResponseDTO(Expense expense) {

        return new ExpenseResponseDTO(
            expense.getExpenseID(), expense.getTitle(), expense.getAmount(),
            expense.getGroup().getGroupID(), expense.getCreatedBy().getUserID(),
            expense.getPaidBy().getUserID(), expense.getCreatedAt(), expense.getSplitType()
        );
    }
    protected ExpenseSplit mapFromExpenseSplitRequest(UUID userID, UUID expenseID, BigDecimal shareAmount) {

        User user = userRepo.findById(userID)
        .orElseThrow(() -> new RecordNotFoundException("User", userID.toString()));

        Expense expense = expenseRepo.findById(expenseID)
        .orElseThrow(() -> new RecordNotFoundException("Expense", expenseID.toString()));

        return new ExpenseSplit(expense, user, shareAmount);
    }
}