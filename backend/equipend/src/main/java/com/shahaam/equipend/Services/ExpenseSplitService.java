package com.shahaam.equipend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.shahaam.equipend.auth.User;
import com.shahaam.equipend.auth.UserRepository;
import com.shahaam.equipend.dto.ExpenseSplitRequestDTO;
import com.shahaam.equipend.dto.ExpenseSplitResponseDTO;
import com.shahaam.equipend.exceptions.RecordNotFoundException;
import com.shahaam.equipend.exceptions.UniqueIdentifierAlreaydExistsException;
import com.shahaam.equipend.models.Expense;
import com.shahaam.equipend.models.ExpenseSplit;
import com.shahaam.equipend.models.ExpenseSplitID;
import com.shahaam.equipend.repositories.ExpenseSplitsRepository;
import com.shahaam.equipend.repositories.ExpensesRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ExpenseSplitService {
    private final ExpenseSplitsRepository expSplitRepo;
    private final UserRepository userRepo;
    private final ExpensesRepository expRepo;

    public ExpenseSplitResponseDTO add(ExpenseSplitRequestDTO req) {

        if(
            expSplitRepo.existsByExpense_ExpenseIDAndUser_UserID(req.expense(), req.user())
        ) throw new UniqueIdentifierAlreaydExistsException("Split");

        if (!userRepo.existsById(req.user()))
            throw new RecordNotFoundException("User", req.user().toString());
    
        if (!expRepo.existsById(req.expense()))
            throw new RecordNotFoundException("Expense", req.expense().toString());

        ExpenseSplit expenseSplit = mapFromExpenseSplitRequestDTO(req);

        expSplitRepo.save(expenseSplit);

        return mapToExpenseSplitResponseDTO(expenseSplit);
    }

    public ExpenseSplitResponseDTO get(UUID expenseID, UUID userID) {

        ExpenseSplit expenseSplit = expSplitRepo.findById(new ExpenseSplitID(expenseID, userID))
        .orElseThrow(() -> new RecordNotFoundException("Split", expenseID.toString(), userID.toString()));

        return mapToExpenseSplitResponseDTO(expenseSplit);
    }
    public List<ExpenseSplitResponseDTO> getByExpense(UUID expenseID) {
        return expSplitRepo.findByExpense_ExpenseID(expenseID)
        .stream().map(this::mapToExpenseSplitResponseDTO).toList();
    }
    public List<ExpenseSplitResponseDTO> getByUser(UUID userID) {
        return expSplitRepo.findByUser_UserID(userID)
        .stream().map(this::mapToExpenseSplitResponseDTO).toList();
    }

    protected ExpenseSplit mapFromExpenseSplitRequestDTO(ExpenseSplitRequestDTO req) {

        User user = userRepo.findById(req.user())
        .orElseThrow(() -> new RecordNotFoundException("User", req.user().toString()));

        Expense expense = expRepo.findById(req.expense())
        .orElseThrow(() -> new RecordNotFoundException("Expense", req.expense().toString()));

        return new ExpenseSplit(expense, user, req.shareAmount());
    }
    protected ExpenseSplitResponseDTO mapToExpenseSplitResponseDTO(ExpenseSplit split) {

        return new ExpenseSplitResponseDTO(
            split.getUser().getUserID(),
            split.getExpense().getExpenseID(),
            split.getShareAmount());
    }
}
