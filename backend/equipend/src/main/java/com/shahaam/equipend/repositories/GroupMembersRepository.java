package com.shahaam.equipend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shahaam.equipend.models.GroupMember;
import com.shahaam.equipend.models.GroupMemberID;

public interface GroupMembersRepository extends JpaRepository<GroupMember, GroupMemberID> {

    boolean existsByGroup_GroupIDAndUser_UserID(UUID groupID, UUID userID);
    List<GroupMember> findByGroup_GroupID(UUID groupID);
    List<GroupMember> findByUser_UserID(UUID userID);    

}
