package com.shahaam.equipend.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shahaam.equipend.models.ExpenseSplit;
import com.shahaam.equipend.models.ExpenseSplitID;

public interface ExpenseSplitsRepository extends JpaRepository<ExpenseSplit, ExpenseSplitID> {

    boolean existsByExpense_ExpenseIDAndUser_UserID(UUID expenseID, UUID userID);

    List<ExpenseSplit> findByExpense_ExpenseID(UUID expenseID);

    List<ExpenseSplit> findByUser_UserID(UUID userID);

    @Query(value = """
        SELECT COALESCE(SUM(es.share_amount), 0)
        FROM expenses e
        JOIN expense_splits es ON e.expense_id = es.expense_id
        WHERE e.group_id = :groupId
          AND e.paid_by = :expensePayer
          AND es.user_id = :debtor
        """, nativeQuery = true)
    BigDecimal allRecordedSplitsBetween(@Param("groupId") UUID groupId,
                                        @Param("expensePayer") UUID expensePayer,
                                        @Param("debtor") UUID debtor);

}