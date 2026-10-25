package com.shahaam.equipend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shahaam.equipend.models.Expense;

public interface ExpensesRepository extends JpaRepository<Expense, UUID> {

    long countByGroup_GroupID(UUID groupID);

    List<Expense> findByGroup_GroupID(UUID groupID);

    List<Expense> findByCreatedBy_UserID(UUID userID);

    List<Expense> findByPaidBy_UserID(UUID userID);
}