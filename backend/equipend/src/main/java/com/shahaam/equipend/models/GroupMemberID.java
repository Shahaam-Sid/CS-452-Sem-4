package com.shahaam.equipend.models;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable 
@NoArgsConstructor 
@AllArgsConstructor 
@Getter 
@Setter 
@EqualsAndHashCode 
public class GroupMemberID implements Serializable{

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "group_id", columnDefinition = "BINARY(16)")
    private UUID groupID;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID userID;
    
}
