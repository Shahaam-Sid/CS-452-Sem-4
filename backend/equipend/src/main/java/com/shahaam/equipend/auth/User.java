package com.shahaam.equipend.auth;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.shahaam.equipend.exceptions.InvalidAccountCreationTime;
import com.shahaam.equipend.exceptions.InvalidDOBException;
import com.shahaam.equipend.utils.UtilityMethods;
import com.shahaam.equipend.utils.ValidationsMethods;

@Entity
@Table(name = "users")
@AllArgsConstructor 
@NoArgsConstructor
public class User implements UserDetails {

    @Getter
    @Id
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "user_id", nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID userID;
    
    @Getter
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Getter
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Getter
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Getter
    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Getter
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public User(String fullName, String username, String email,
        String passwordHash, LocalDate dob,
        String country
    ) {
        setGeneratedUserID();
        setFullName(fullName);
        setUsername(username);
        setEmail(email);
        setPasswordHash(passwordHash);
        setDob(dob);
        setCountry(country);
        setCurrentCreatedAt();
    }

    public void setGeneratedUserID() {
        this.userID = UtilityMethods.generateUUIDv7();
    }
    public void setUserID(UUID userID) {
        ValidationsMethods.checkNullObject(userID, "User ID");
        this.userID = userID;
    }
    public void setFullName(String name) {
        ValidationsMethods.checkNullString(name, "Name");
        ValidationsMethods.checkLengthString(name, 3, 100, "Name");
        ValidationsMethods.isStringAplhaOnly(name, "Name");

        this.fullName = name;
    }
    public void setUsername(String username) {
        ValidationsMethods.checkNullString(username, "Username");
        ValidationsMethods.checkLengthString(username, 3, 50, "Username");
        ValidationsMethods.isAlphaNumericUnderscoreDot(username, "Username");

        this.username = username.toLowerCase();
    }
    public void setEmail(String email) {
        ValidationsMethods.checkNullString(email, "Email");
        ValidationsMethods.checkLengthString(email,5, 255, "Email");
        ValidationsMethods.validateEmail(email);

        this.email = email.toLowerCase();

    }
    public void setDob(LocalDate dob) {
        ValidationsMethods.checkNullObject(dob, "Date of Birth");

        LocalDate cutOff = LocalDate.now().minusYears(13);
        
        if (dob.isAfter(cutOff)) {
            throw new InvalidDOBException("User must be atleast 13 years old");
        }

        if (dob.isBefore(LocalDate.now().minusYears(100)))
            throw new InvalidDOBException("Age out of acceptable range");
        
        this.dob = dob;
    }
    public void setCountry(String country) {
        ValidationsMethods.checkNullString(country, "Country");
        ValidationsMethods.checkLengthString(country, 3, 50, "Country");

        this.country = country;
    }
    public void setCreatedAt(Instant createdAt) {
        ValidationsMethods.checkNullObject(createdAt, "Created At Timestamp");
        if (createdAt.isAfter(Instant.now()))
            throw new InvalidAccountCreationTime("Account cannot be created in th future");
        this.createdAt = createdAt;
    }
    public void setCurrentCreatedAt() {
        this.createdAt = Instant.now();
    }
    public void setPasswordHash(String passwordHash) {
        ValidationsMethods.checkNullString(passwordHash, "Password Hash");
        this.passwordHash = passwordHash;
    }

    @Override 
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public String getPassword() {
        return passwordHash;
    }
}