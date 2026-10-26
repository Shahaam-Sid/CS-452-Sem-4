package com.shahaam.equipend.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shahaam.equipend.auth.User;
import com.shahaam.equipend.auth.UserRepository;
import com.shahaam.equipend.dto.GroupRequestDTO;
import com.shahaam.equipend.dto.GroupResponseDTO;
import com.shahaam.equipend.exceptions.RecordNotFoundException;
import com.shahaam.equipend.interfaces.ServicesInterface;
import com.shahaam.equipend.models.Group;
import com.shahaam.equipend.models.GroupMember;
import com.shahaam.equipend.repositories.GroupMembersRepository;
import com.shahaam.equipend.repositories.GroupsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService implements ServicesInterface<GroupResponseDTO, GroupRequestDTO, Void> {
    private final GroupsRepository groupRepo;
    private final UserRepository userRepo;
    private final GroupMembersRepository grpMemRepo;

    @Override
    @Transactional 
    public GroupResponseDTO add(GroupRequestDTO req) {
        
        if (!userRepo.existsById(req.createdBy()))
            throw new RecordNotFoundException("User", req.createdBy().toString());
    
        Group group = mapFromGroupRequestDTO(req);

        groupRepo.save(group);

        grpMemRepo.save(new GroupMember(group, group.getCreatedBy()));

        return mapToGroupResponseDTO(group);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupResponseDTO get(UUID id) {
        
        Group group = groupRepo.findById(id).orElseThrow(
            () -> new RecordNotFoundException("Group", id.toString())
        );

        return mapToGroupResponseDTO(group);
    }

    protected Group mapFromGroupRequestDTO(GroupRequestDTO req) {

        User user = userRepo.findById(req.createdBy()).orElseThrow(
            () -> new RecordNotFoundException("User", req.createdBy().toString())
        );

        return new Group(req.name(), req.description(), req.currency(), user);
    }
    protected GroupResponseDTO mapToGroupResponseDTO(Group group) {
        
        return new GroupResponseDTO(
            group.getGroupID(), group.getName(), group.getDescription(), group.getCurrency(),
            group.getCreatedBy().getUserID(), group.getCreatedAt()
        );
    }
}