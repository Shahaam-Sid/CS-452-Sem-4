package com.shahaam.equipend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.shahaam.equipend.auth.User;
import com.shahaam.equipend.auth.UserRepository;
import com.shahaam.equipend.dto.SettlementRequestDTO;
import com.shahaam.equipend.dto.SettlementResponseDTO;
import com.shahaam.equipend.exceptions.AmountMismatchExpection;
import com.shahaam.equipend.exceptions.RecordNotFoundException;
import com.shahaam.equipend.models.Group;
import com.shahaam.equipend.models.Settlement;
import com.shahaam.equipend.repositories.ExpenseSplitsRepository;
import com.shahaam.equipend.repositories.GroupsRepository;
import com.shahaam.equipend.repositories.SettlementsRepository;

@ExtendWith(MockitoExtension.class)
class SettlementServiceTest {

    @Mock
    private SettlementsRepository settlementRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private GroupsRepository groupRepo;

    @Mock
    private ExpenseSplitsRepository expSpltRepo;

    @InjectMocks
    private SettlementService settlementService;

    private UUID settlementId;
    private UUID paidToId;
    private UUID paidById;
    private UUID groupId;
    private BigDecimal amount;
    private SettlementRequestDTO requestDTO;
    private User mockPaidToUser;
    private User mockPaidByUser;
    private Group mockGroup;
    private Settlement mockSettlement;

    @BeforeEach
    void setUp() {
        settlementId = UUID.randomUUID();
        paidToId = UUID.randomUUID();
        paidById = UUID.randomUUID();
        groupId = UUID.randomUUID();
        amount = new BigDecimal("100.00");

        requestDTO = new SettlementRequestDTO("Dinner settlement", amount, paidToId, paidById, groupId);

        mockPaidToUser = new User();
        mockPaidToUser.setUserID(paidToId);

        mockPaidByUser = new User();
        mockPaidByUser.setUserID(paidById);

        mockGroup = new Group();
        mockGroup.setGroupID(groupId);

        mockSettlement = new Settlement("Dinner settlement", amount, mockPaidToUser, mockPaidByUser, mockGroup);
        mockSettlement.setSettlementID(settlementId);
        mockSettlement.setTimestamp(Instant.now());
    }

    // --- GET (SINGLE) TESTS ---

    @Test
    @DisplayName("Should return SettlementResponseDTO when valid settlement ID is provided")
    void get_Success() {
        // Arrange
        when(settlementRepo.findById(settlementId)).thenReturn(Optional.of(mockSettlement));

        // Act
        SettlementResponseDTO response = settlementService.get(settlementId);

        // Assert
        assertNotNull(response);
        assertEquals(settlementId, response.settlementID());
        assertEquals("Dinner settlement", response.note());
        assertEquals(amount, response.amount());
        assertEquals(paidToId, response.paidTo());
        assertEquals(paidById, response.paidBy());
        assertEquals(groupId, response.group());
        verify(settlementRepo).findById(settlementId);
    }

    @Test
    @DisplayName("Should throw RecordNotFoundException when settlement is not found by ID")
    void get_ThrowsWhenNotFound() {
        // Arrange
        when(settlementRepo.findById(settlementId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> settlementService.get(settlementId)
        );
        verify(settlementRepo).findById(settlementId);
    }

    // --- ADD TESTS ---

    @Test
    @DisplayName("Should add settlement successfully when validation passes")
    void add_Success() {
        // Arrange
        when(userRepo.existsById(paidToId)).thenReturn(true);
        when(userRepo.existsById(paidById)).thenReturn(true);
        when(groupRepo.existsById(groupId)).thenReturn(true);
        
        // Split and existing settlement checks
        when(expSpltRepo.allRecordedSplitsBetween(paidById, paidToId)).thenReturn(new BigDecimal("150.00"));
        when(settlementRepo.allRecordedSettlementBetween(paidById, paidToId)).thenReturn(new BigDecimal("20.00"));

        // Mocks for mapping inside add()
        when(userRepo.findById(paidToId)).thenReturn(Optional.of(mockPaidToUser));
        when(userRepo.findById(paidById)).thenReturn(Optional.of(mockPaidByUser));
        when(groupRepo.findById(groupId)).thenReturn(Optional.of(mockGroup));
        when(settlementRepo.save(any(Settlement.class))).thenReturn(mockSettlement);

        // Act
        SettlementResponseDTO response = settlementService.add(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(amount, response.amount());
        assertEquals(paidToId, response.paidTo());
        assertEquals(paidById, response.paidBy());
        assertEquals(groupId, response.group());

        verify(userRepo).existsById(paidToId);
        verify(userRepo).existsById(paidById);
        verify(groupRepo).existsById(groupId);
        verify(expSpltRepo).allRecordedSplitsBetween(paidById, paidToId);
        verify(settlementRepo).allRecordedSettlementBetween(paidById, paidToId);
        verify(settlementRepo).save(any(Settlement.class));
    }

    @Test
    @DisplayName("Should throw RecordNotFoundException when 'paidTo' user does not exist")
    void add_ThrowsWhenPaidToUserNotFound() {
        // Arrange
        when(userRepo.existsById(paidToId)).thenReturn(false);

        // Act & Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> settlementService.add(requestDTO)
        );

        verify(userRepo).existsById(paidToId);
        verify(userRepo, never()).existsById(paidById);
        verify(settlementRepo, never()).save(any());
    }

    @Test
    @DisplayName("Should throw RecordNotFoundException when 'paidBy' user does not exist")
    void add_ThrowsWhenPaidByUserNotFound() {
        // Arrange
        when(userRepo.existsById(paidToId)).thenReturn(true);
        when(userRepo.existsById(paidById)).thenReturn(false);

        // Act & Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> settlementService.add(requestDTO)
        );

        verify(userRepo).existsById(paidById);
        verify(settlementRepo, never()).save(any());
    }

    @Test
    @DisplayName("Should throw RecordNotFoundException when group does not exist")
    void add_ThrowsWhenGroupNotFound() {
        // Arrange
        when(userRepo.existsById(paidToId)).thenReturn(true);
        when(userRepo.existsById(paidById)).thenReturn(true);
        when(groupRepo.existsById(groupId)).thenReturn(false);

        // Act & Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> settlementService.add(requestDTO)
        );

        verify(groupRepo).existsById(groupId);
        verify(settlementRepo, never()).save(any());
    }

    @Test
    @DisplayName("Should throw AmountMismatchExpection when allRecordedSplitsBetween returns ZERO")
    void add_ThrowsWhenSplitsAreZero() {
        // Arrange
        when(userRepo.existsById(paidToId)).thenReturn(true);
        when(userRepo.existsById(paidById)).thenReturn(true);
        when(groupRepo.existsById(groupId)).thenReturn(true);
        when(expSpltRepo.allRecordedSplitsBetween(paidById, paidToId)).thenReturn(BigDecimal.ZERO);

        // Act & Assert
        assertThrows(
                AmountMismatchExpection.class,
                () -> settlementService.add(requestDTO)
        );

        verify(expSpltRepo).allRecordedSplitsBetween(paidById, paidToId);
        verify(settlementRepo, never()).allRecordedSettlementBetween(any(), any());
        verify(settlementRepo, never()).save(any());
    }

    @Test
    @DisplayName("Should throw AmountMismatchExpection when total settlements exceed total splits")
    void add_ThrowsWhenSettlementsExceedSplits() {
        // Arrange
        when(userRepo.existsById(paidToId)).thenReturn(true);
        when(userRepo.existsById(paidById)).thenReturn(true);
        when(groupRepo.existsById(groupId)).thenReturn(true);
        when(expSpltRepo.allRecordedSplitsBetween(paidById, paidToId)).thenReturn(new BigDecimal("100.00"));
        // Existing settlements = 50.00, requested amount = 60.00 -> total 110.00 > 100.00
        when(settlementRepo.allRecordedSettlementBetween(paidById, paidToId)).thenReturn(new BigDecimal("50.00"));

        // Act & Assert
        assertThrows(
                AmountMismatchExpection.class,
                () -> settlementService.add(requestDTO)
        );

        verify(settlementRepo).allRecordedSettlementBetween(paidById, paidToId);
        verify(settlementRepo, never()).save(any());
    }

    // --- COUNT & LIST QUERY TESTS ---

    @Test
    @DisplayName("Should return group settlement count successfully")
    void countGroupSettlements_Success() {
        // Arrange
        when(settlementRepo.countByGroup_GroupID(groupId)).thenReturn(5L);

        // Act
        Long count = settlementService.countGroupSettlements(groupId);

        // Assert
        assertEquals(5L, count);
        verify(settlementRepo).countByGroup_GroupID(groupId);
    }

    @Test
    @DisplayName("Should return list of settlements for a group")
    void getGroupSettlements_Success() {
        // Arrange
        when(settlementRepo.findByGroup_GroupID(groupId)).thenReturn(List.of(mockSettlement));

        // Act
        List<SettlementResponseDTO> results = settlementService.getGroupSettlements(groupId);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(groupId, results.get(0).group());
        verify(settlementRepo).findByGroup_GroupID(groupId);
    }

    @Test
    @DisplayName("Should return list of settlements by 'paidTo' user ID")
    void getByPaidTo_Success() {
        // Arrange
        when(settlementRepo.findByPaidTo_UserID(paidToId)).thenReturn(List.of(mockSettlement));

        // Act
        List<SettlementResponseDTO> results = settlementService.getByPaidTo(paidToId);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(paidToId, results.get(0).paidTo());
        verify(settlementRepo).findByPaidTo_UserID(paidToId);
    }

    @Test
    @DisplayName("Should return list of settlements by payer 'paidBy' user ID")
    void getByPayer_Success() {
        // Arrange
        when(settlementRepo.findByPaidBy_UserID(paidById)).thenReturn(List.of(mockSettlement));

        // Act
        List<SettlementResponseDTO> results = settlementService.getByPayer(paidById);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(paidById, results.get(0).paidBy());
        verify(settlementRepo).findByPaidBy_UserID(paidById);
    }

    @Test
    @DisplayName("Should return list of settlements filtered by group and 'paidTo' user")
    void getByGroupPaidTo_Success() {
        // Arrange
        when(settlementRepo.findByGroup_GroupIDAndPaidTo_UserID(groupId, paidToId)).thenReturn(List.of(mockSettlement));

        // Act
        List<SettlementResponseDTO> results = settlementService.getByGroupPaidTo(groupId, paidToId);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        verify(settlementRepo).findByGroup_GroupIDAndPaidTo_UserID(groupId, paidToId);
    }

    @Test
    @DisplayName("Should return list of settlements filtered by group and 'paidBy' user")
    void getByGroupPaidBy_Success() {
        // Arrange
        when(settlementRepo.findByGroup_GroupIDAndPaidBy_UserID(groupId, paidById)).thenReturn(List.of(mockSettlement));

        // Act
        List<SettlementResponseDTO> results = settlementService.getByGroupPaidBy(groupId, paidById);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        verify(settlementRepo).findByGroup_GroupIDAndPaidBy_UserID(groupId, paidById);
    }
}