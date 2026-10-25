package com.shahaam.equipend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shahaam.equipend.auth.User;
import com.shahaam.equipend.auth.UserRepository;
import com.shahaam.equipend.dto.GroupMemberRequestDTO;
import com.shahaam.equipend.dto.GroupMemberResponseDTO;
import com.shahaam.equipend.exceptions.RecordNotFoundException;
import com.shahaam.equipend.exceptions.UniqueIdentifierAlreaydExistsException;
import com.shahaam.equipend.models.Group;
import com.shahaam.equipend.models.GroupMember;
import com.shahaam.equipend.models.GroupMemberID;
import com.shahaam.equipend.repositories.GroupMembersRepository;
import com.shahaam.equipend.repositories.GroupsRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class GroupMemberService {
    
    private final GroupMembersRepository grpMemberRepo;
    private final GroupsRepository groupsRepo;
    private final UserRepository userRepo;

    @Transactional 
    public GroupMemberResponseDTO add(GroupMemberRequestDTO req) {

        if (grpMemberRepo.existsByGroup_GroupIDAndUser_UserID(req.group(), req.user()))
            throw new UniqueIdentifierAlreaydExistsException("Group Membership");
        
        if (!userRepo.existsById(req.user()))
            throw new RecordNotFoundException("User", req.user().toString());

        if (!groupsRepo.existsById(req.group()))
            throw new RecordNotFoundException("Group", req.group().toString());

        GroupMember grpMember = mapFromGroupMemberRequestDTO(req);

        grpMemberRepo.save(grpMember);

        return mapToGroupMemberResponseDTO(grpMember);
    
    }
    @Transactional(readOnly = true)
    public GroupMemberResponseDTO get(UUID groupID, UUID userID) {
        
        GroupMember grpMember = grpMemberRepo.findById(new GroupMemberID(groupID, userID))
        .orElseThrow(() -> new RecordNotFoundException(
            "Group Membership", groupID.toString(), userID.toString()
        ));

        return mapToGroupMemberResponseDTO(grpMember);
    }
    @Transactional(readOnly = true)
    public List<GroupMemberResponseDTO> getByGroup(UUID groupID) {

        return grpMemberRepo.findByGroup_GroupID(groupID).stream().map(this::mapToGroupMemberResponseDTO).toList();
    }
    @Transactional(readOnly = true)
    public List<GroupMemberResponseDTO> getByUser(UUID userID) {

        return grpMemberRepo.findByUser_UserID(userID).stream().map(this::mapToGroupMemberResponseDTO).toList();
    }

    protected GroupMember mapFromGroupMemberRequestDTO(GroupMemberRequestDTO req) {

        User user = userRepo.findById(req.user()).orElseThrow(
            () -> new RecordNotFoundException("User", req.user().toString())
        );

        Group group = groupsRepo.findById(req.group()).orElseThrow(
            () -> new RecordNotFoundException("Group", req.group().toString())
        );
    
        return new GroupMember(group, user);
    }
    protected GroupMemberResponseDTO mapToGroupMemberResponseDTO(GroupMember grpMember) {
        
        return new GroupMemberResponseDTO(
            grpMember.getUser().getUserID(),
            grpMember.getGroup().getGroupID(),
            grpMember.getJoinedAt()
        );
    }
}