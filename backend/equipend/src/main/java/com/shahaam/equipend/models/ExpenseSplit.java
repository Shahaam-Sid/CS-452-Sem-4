package com.shahaam.equipend.models;

import java.math.BigDecimal;

import com.shahaam.equipend.auth.User;

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
}
