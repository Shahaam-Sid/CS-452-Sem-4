package com.shahaam.equipend.models;

import java.math.BigDecimal;

import com.shahaam.equipend.auth.User;
import com.shahaam.equipend.exceptions.InvalidAmountException;
import com.shahaam.equipend.utils.ValidationsMethods;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "expense_splits")
@AllArgsConstructor 
@NoArgsConstructor 
@Getter 
public class ExpenseSplit {

    @EmbeddedId 
    private ExpenseSplitID id = new ExpenseSplitID();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("expenseID")
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userID")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "share_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal shareAmount;

    public ExpenseSplit(Expense expense, User user, BigDecimal shareAmount) {
        id.setExpenseID(expense.getExpenseID());
        id.setUserID(user.getUserID());
        setExpense(expense);
        setUser(user);
        setShareAmount(shareAmount);
    }

    public void setExpense(Expense expense) {
        ValidationsMethods.checkNullObject(expense, "Expense");

        this.expense = expense;
    }
    public void setUser(User user) {
        ValidationsMethods.checkNullObject(user, "User");
    
        this.user = user;
    }
    public void setShareAmount(BigDecimal shareAmount) {
        if (shareAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Share Amount cannot <= 0");
        }

        this.shareAmount = shareAmount;
    }
}
