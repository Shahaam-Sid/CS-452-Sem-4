package com.shahaam.equipend.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.shahaam.equipend.auth.User;
import com.shahaam.equipend.exceptions.InvalidAmountException;
import com.shahaam.equipend.utils.UtilityMethods;
import com.shahaam.equipend.utils.ValidationsMethods;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "settlement")
@AllArgsConstructor 
@NoArgsConstructor
@Getter 
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Settlement {

    @Id
    @JdbcTypeCode(SqlTypes.BINARY)
    @EqualsAndHashCode.Include
    @Column(name = "settlement_id", columnDefinition = "BINARY(16)")
    private UUID settlementID;

    @Column(name = "note", length = 255, nullable = false)
    private String note;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @CreationTimestamp 
    @Column(name = "time", nullable = false, updatable = false)
    private Instant timestamp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paid_to", nullable = false)
    private User paidTo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paid_by", nullable = false)
    private User paidBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    public Settlement(String note, BigDecimal amount, User paidTo, User paidBy, Group group) {
        setGeneratedSettlementID();
        setNote(note);
        setAmount(amount);
        setPaidTo(paidTo);
        setPaidBy(paidBy);
        setGroup(group);
    }

    public void setSettlementID(UUID id) {
        ValidationsMethods.checkNullObject(id, "Settlement ID");
        
        this.settlementID = id;
    }
    public void setGeneratedSettlementID() {
        this.settlementID = UtilityMethods.generateUUIDv7();
    }
    public void setNote(String note) {
        if (note != null) {
            ValidationsMethods.checkLengthString(note, 1, 255, "Note");
            
            this.note = note;
        }
        this.note = "";
    }
    public void setAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidAmountException("Settlement Amount cannot be negative");

        this.amount = amount;
    }
    public void setTimestamp(Instant timestamp) {
        ValidationsMethods.checkNullObject(timestamp, "Timestamp");
        ValidationsMethods.checkTimeStamp(timestamp, Instant.now(), "Settlement cannot be done in the future");

        this.timestamp = timestamp;
    }
    public void setPaidBy(User user) {
        ValidationsMethods.checkNullObject(user, "User paying cannot be null");

        this.paidBy = user;
    }
    public void setPaidTo(User user) {
        ValidationsMethods.checkNullObject(user, "User paid cannot be null");

        this.paidTo = user;
    }
    public void setGroup(Group group) {
        ValidationsMethods.checkNullObject(group, "Group cannot be null");

        this.group = group;
    }
}