package com.shahaam.equipend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shahaam.equipend.models.ExpenseSplit;
import com.shahaam.equipend.models.ExpenseSplitID;

public interface ExpenseSplitsRepository extends JpaRepository<ExpenseSplit, ExpenseSplitID> {

    boolean existsByExpense_ExpenseIDAndUser_UserID(UUID expenseID, UUID userID);

    List<ExpenseSplit> findByExpense_ExpenseID(UUID expenseID);

    List<ExpenseSplit> findByUser_UserID(UUID userID);

}