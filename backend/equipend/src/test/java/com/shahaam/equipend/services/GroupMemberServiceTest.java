package com.shahaam.equipend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
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
import com.shahaam.equipend.dto.GroupMemberRequestDTO;
import com.shahaam.equipend.dto.GroupMemberResponseDTO;
import com.shahaam.equipend.exceptions.RecordNotFoundException;
import com.shahaam.equipend.exceptions.UniqueIdentifierAlreaydExistsException;
import com.shahaam.equipend.models.Group;
import com.shahaam.equipend.models.GroupMember;
import com.shahaam.equipend.models.GroupMemberID;
import com.shahaam.equipend.repositories.GroupMembersRepository;
import com.shahaam.equipend.repositories.GroupsRepository;

@ExtendWith(MockitoExtension.class)
class GroupMemberServiceTest {

    @Mock
    private GroupMembersRepository grpMemberRepo;
    @Mock
    private GroupsRepository groupsRepo;
    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private GroupMemberService groupMemberService;

    private UUID userId;
    private UUID groupId;
    private User user;
    private Group group;
    private GroupMember groupMember;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        groupId = UUID.randomUUID();

        user = new User();
        user.setUserID(userId);

        group = new Group();
        group.setGroupID(groupId);

        groupMember = new GroupMember(group, user);
        groupMember.setJoinedAt(Instant.now());
    }

    @Test
    void testAddGroupMember_Success() {
        GroupMemberRequestDTO request = new GroupMemberRequestDTO(
                userId,
                groupId,
                Instant.now()
        );

        when(grpMemberRepo.existsByGroup_GroupIDAndUser_UserID(groupId, userId)).thenReturn(false);
        when(userRepo.existsById(userId)).thenReturn(true);
        when(groupsRepo.existsById(groupId)).thenReturn(true);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(groupsRepo.findById(groupId)).thenReturn(Optional.of(group));
        when(grpMemberRepo.save(any(GroupMember.class))).thenReturn(groupMember);

        GroupMemberResponseDTO response = groupMemberService.add(request);

        assertNotNull(response);
        assertEquals(userId, response.user());
        assertEquals(groupId, response.group());

        verify(grpMemberRepo, times(1)).existsByGroup_GroupIDAndUser_UserID(groupId, userId);
        verify(userRepo, times(1)).existsById(userId);
        verify(groupsRepo, times(1)).existsById(groupId);
        verify(grpMemberRepo, times(1)).save(any(GroupMember.class));
    }

    @Test
    void testAddGroupMember_AlreadyExists_ThrowsException() {
        GroupMemberRequestDTO request = new GroupMemberRequestDTO(
                userId,
                groupId,
                Instant.now()
        );

        // Simulate membership already existing
        when(grpMemberRepo.existsByGroup_GroupIDAndUser_UserID(groupId, userId)).thenReturn(true);

        assertThrows(
                UniqueIdentifierAlreaydExistsException.class,
                () -> groupMemberService.add(request)
        );

        verify(grpMemberRepo, times(1)).existsByGroup_GroupIDAndUser_UserID(groupId, userId);
        verify(userRepo, never()).existsById(any());
        verify(grpMemberRepo, never()).save(any());
    }

    @Test
    void testAddGroupMember_UserNotFound_ThrowsException() {
        GroupMemberRequestDTO request = new GroupMemberRequestDTO(
                userId,
                groupId,
                Instant.now()
        );

        when(grpMemberRepo.existsByGroup_GroupIDAndUser_UserID(groupId, userId)).thenReturn(false);
        when(userRepo.existsById(userId)).thenReturn(false);

        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> groupMemberService.add(request)
        );

        assertTrue(exception.getMessage().contains("User"));
        verify(groupsRepo, never()).existsById(any());
        verify(grpMemberRepo, never()).save(any());
    }

    @Test
    void testAddGroupMember_GroupNotFound_ThrowsException() {
        GroupMemberRequestDTO request = new GroupMemberRequestDTO(
                userId,
                groupId,
                Instant.now()
        );

        when(grpMemberRepo.existsByGroup_GroupIDAndUser_UserID(groupId, userId)).thenReturn(false);
        when(userRepo.existsById(userId)).thenReturn(true);
        when(groupsRepo.existsById(groupId)).thenReturn(false);

        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> groupMemberService.add(request)
        );

        assertTrue(exception.getMessage().contains("Group"));
        verify(grpMemberRepo, never()).save(any());
    }

    @Test
    void testGetGroupMember_Success() {
        when(grpMemberRepo.findById(any(GroupMemberID.class))).thenReturn(Optional.of(groupMember));

        GroupMemberResponseDTO response = groupMemberService.get(groupId, userId);

        assertNotNull(response);
        assertEquals(userId, response.user());
        assertEquals(groupId, response.group());
        verify(grpMemberRepo, times(1)).findById(any(GroupMemberID.class));
    }

    @Test
    void testGetGroupMember_NotFound_ThrowsException() {
        when(grpMemberRepo.findById(any(GroupMemberID.class))).thenReturn(Optional.empty());

        assertThrows(
                RecordNotFoundException.class,
                () -> groupMemberService.get(groupId, userId)
        );
    }

    @Test
    void testGetByGroup() {
        when(grpMemberRepo.findByGroup_GroupID(groupId)).thenReturn(List.of(groupMember));

        List<GroupMemberResponseDTO> members = groupMemberService.getByGroup(groupId);

        assertEquals(1, members.size());
        assertEquals(groupId, members.get(0).group());
        verify(grpMemberRepo, times(1)).findByGroup_GroupID(groupId);
    }

    @Test
    void testGetByUser() {
        when(grpMemberRepo.findByUser_UserID(userId)).thenReturn(List.of(groupMember));

        List<GroupMemberResponseDTO> members = groupMemberService.getByUser(userId);

        assertEquals(1, members.size());
        assertEquals(userId, members.get(0).user());
        verify(grpMemberRepo, times(1)).findByUser_UserID(userId);
    }
}