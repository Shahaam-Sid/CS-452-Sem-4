package com.shahaam.equipend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.shahaam.equipend.auth.User;
import com.shahaam.equipend.auth.UserRepository;
import com.shahaam.equipend.dto.GroupRequestDTO;
import com.shahaam.equipend.dto.GroupResponseDTO;
import com.shahaam.equipend.exceptions.RecordNotFoundException;
import com.shahaam.equipend.models.Group;
import com.shahaam.equipend.models.GroupMember;
import com.shahaam.equipend.repositories.GroupMembersRepository;
import com.shahaam.equipend.repositories.GroupsRepository;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupsRepository groupRepo;
    @Mock
    private UserRepository userRepo;
    @Mock
    private GroupMembersRepository grpMemRepo;

    @InjectMocks
    private GroupService groupService;

    private UUID userId;
    private UUID groupId;
    private User user;
    private Group group;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        groupId = UUID.randomUUID();

        user = new User();
        user.setUserID(userId);

        group = new Group();
        group.setGroupID(groupId);
        group.setName("Trip to Paris");
        group.setDescription("Summer vacation expenses");
        group.setCurrency("USD");
        group.setCreatedBy(user);
        group.setCreatedAt(Instant.now());
    }

    @Test
    void testAddGroup_Success() {
        GroupRequestDTO request = new GroupRequestDTO(
                "Trip to Paris",
                "Summer vacation expenses",
                "USD",
                userId
        );

        // Mock repository behaviors
        when(userRepo.existsById(userId)).thenReturn(true);
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(groupRepo.save(any(Group.class))).thenReturn(group);
        when(grpMemRepo.save(any(GroupMember.class))).thenReturn(new GroupMember());

        GroupResponseDTO response = groupService.add(request);

        // Assertions
        assertNotNull(response);
        assertEquals("Trip to Paris", response.name());
        assertEquals("usd", response.currency());
        assertEquals(userId, response.createdBy());

        // Verify interactions
        verify(userRepo, times(1)).existsById(userId);
        verify(userRepo, times(1)).findById(userId);
        verify(groupRepo, times(1)).save(any(Group.class));
        verify(grpMemRepo, times(1)).save(any(GroupMember.class));
    }

    @Test
    void testAddGroup_UserNotFound_ThrowsException() {
        GroupRequestDTO request = new GroupRequestDTO(
                "Trip to Paris",
                "Summer vacation expenses",
                "USD",
                userId
        );

        // Simulate user not existing
        when(userRepo.existsById(userId)).thenReturn(false);

        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> groupService.add(request)
        );

        assertTrue(exception.getMessage().contains("User"));
        
        // Ensure downstream repo calls are never made
        verify(groupRepo, never()).save(any());
        verify(grpMemRepo, never()).save(any());
    }

    @Test
    void testGetGroup_Success() {
        when(groupRepo.findById(groupId)).thenReturn(Optional.of(group));

        GroupResponseDTO response = groupService.get(groupId);

        assertNotNull(response);
        assertEquals(groupId, response.groupID());
        assertEquals("Trip to Paris", response.name());

        verify(groupRepo, times(1)).findById(groupId);
    }

    @Test
    void testGetGroup_NotFound_ThrowsException() {
        when(groupRepo.findById(groupId)).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> groupService.get(groupId)
        );

        assertTrue(exception.getMessage().contains("Group"));
        verify(groupRepo, times(1)).findById(groupId);
    }
}