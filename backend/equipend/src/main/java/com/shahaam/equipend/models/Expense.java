package com.shahaam.equipend.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.shahaam.equipend.auth.User;
import com.shahaam.equipend.enums.SplitType;
import com.shahaam.equipend.exceptions.InvalidAmountException;
import com.shahaam.equipend.utils.UtilityMethods;
import com.shahaam.equipend.utils.ValidationsMethods;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "expenses")
@AllArgsConstructor
@NoArgsConstructor 
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Expense {

    @Id
    @JdbcTypeCode(SqlTypes.BINARY)
    @EqualsAndHashCode.Include
    @Column(name = "expense_id", columnDefinition = "BINARY(16)")
    private UUID expenseID;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @CreationTimestamp 
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paid_by", nullable = false)
    private User paidBy;

    @Column(name = "split_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SplitType splitType;

    public Expense(
        String title, BigDecimal amount, Group group,
        User createdBy, User paidBy, SplitType splitType
    ) {
        setGeneratedExpenseID();
        setTitle(title);
        setAmount(amount);
        setGroup(group);
        setCreatedBy(createdBy);
        setPaidBy(paidBy);
        setSplitType(splitType);        
    }

    public void setExpenseID(UUID id) {
        ValidationsMethods.checkNullObject(id, "UUID");

        this.expenseID = id;
    }
    public void setGeneratedExpenseID() {
        this.expenseID = UtilityMethods.generateUUIDv7();
    }
    public void setTitle(String title) {
        ValidationsMethods.checkNullString(title, "Title");
        ValidationsMethods.checkLengthString(title, 5, 100, "Title");

        this.title = title;
    }
    public void setCreatedAt(Instant timestamp) {
        ValidationsMethods.checkNullObject(timestamp, "Expense Created At");
        ValidationsMethods.checkTimeStamp(timestamp, Instant.now(), "Expense Created At");
    
        this.createdAt = timestamp;
    }
    public void setAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Expense Amount cannot <= 0");
        }

        this.amount = amount;
    }
    public void setGroup(Group group) {
        ValidationsMethods.checkNullObject(group, "Group");

        this.group = group;
    }
    public void setCreatedBy(User user) {
        ValidationsMethods.checkNullObject(user, "Expense Created By");

        this.createdBy = user;
    }
    public void setPaidBy(User user) {
        ValidationsMethods.checkNullObject(user, "Expense Paid By");

        this.createdBy = user;
    }
    public void setSplitType(SplitType splitType) {

        this.splitType = splitType;
    }
}