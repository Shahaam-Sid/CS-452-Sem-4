package com.shahaam.equipend.models;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.shahaam.equipend.auth.User;
import com.shahaam.equipend.exceptions.InvalidDomainException;
import com.shahaam.equipend.utils.AppConstants;
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
@Table(name = "`groups`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Group {

    @Id
    @JdbcTypeCode(SqlTypes.BINARY)
    @EqualsAndHashCode.Include
    @Column(name = "group_id", nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID groupID;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = true, length = 200)
    private String description;

    @Column(name = "currency", nullable = false, length = 100)
    private String currency;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @CreationTimestamp 
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Group(String name, String description, String currency, User createdBy) {
        setGeneratedGroupID();
        setName(name);
        setDescription(description);
        setCurrency(currency);
        setCreatedBy(createdBy);
    }

    public void setGroupID(UUID groupID) {
        ValidationsMethods.checkNullObject(groupID, "Group ID");
        this.groupID = groupID;
    }
    public void setGeneratedGroupID() {
        this.groupID = UtilityMethods.generateUUIDv7();
    }
    public void setName(String name) {
        ValidationsMethods.checkNullString(name, "Group Name");
        ValidationsMethods.checkLengthString(name, 1, 50, "Group Name");

        this.name = name;
    }
    public void setDescription(String desc) {
        if (desc != null) {
            ValidationsMethods.checkLengthString(desc, 0, 200, "Group Description can be Maximum of 200 Characters");

            this.description = desc;
        }
        this.description = "";
    }
    public void setCurrency(String currency) {
        ValidationsMethods.checkNullString(currency, "Currency");
        ValidationsMethods.checkLengthString(currency, 1,10, "Currency");
        currency = currency.toLowerCase();
        if (!AppConstants.CURRENCIES.containsKey(currency))
            throw new InvalidDomainException("Currency", currency);

        this.currency = currency;
    }
    public void setCreatedBy(User user) {
        this.createdBy = user;
    }
    public void setCreatedAt(Instant createdAt) {
        ValidationsMethods.checkNullObject(createdAt, "Created at Timestamp");
        ValidationsMethods.checkTimeStamp(createdAt, Instant.now(), currency);

        this.createdAt = createdAt;
    }
}


// TODO: update DB README as name varchar updates to 50 characters