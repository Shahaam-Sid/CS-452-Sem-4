package com.shahaam.equipend.models;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.shahaam.equipend.auth.User;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "group_members")
@AllArgsConstructor
@NoArgsConstructor
@Getter 
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GroupMember {

    @EmbeddedId 
    private GroupMemberID id = new GroupMemberID();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupID")
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userID")
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private Instant joinedAt;

    public GroupMember(Group group, User user) {
        id.setGroupID(group.getGroupID());
        id.setUserID(user.getUserID());
        setGoup(group);
        setUser(user);
    }

    public void setGoup(Group group) {
        ValidationsMethods.checkNullObject(group, "Group");

        this.group = group;
    }
    public void setUser(User user) {
        ValidationsMethods.checkNullObject(user, "User");

        this.user = user;
    }
    public void setJoinedAt(Instant joinedAt) {
        ValidationsMethods.checkNullObject(joinedAt, "Timestamp");
        ValidationsMethods.checkTimeStamp(joinedAt, Instant.now(), "Joined At cannot be in the future");

        this.joinedAt = joinedAt;
    }
}