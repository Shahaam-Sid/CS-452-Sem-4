package com.shahaam.equipend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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
import com.shahaam.equipend.dto.ExpenseSplitRequestDTO;
import com.shahaam.equipend.dto.ExpenseSplitResponseDTO;
import com.shahaam.equipend.exceptions.RecordNotFoundException;
import com.shahaam.equipend.exceptions.UniqueIdentifierAlreaydExistsException;
import com.shahaam.equipend.models.Expense;
import com.shahaam.equipend.models.ExpenseSplit;
import com.shahaam.equipend.models.ExpenseSplitID;
import com.shahaam.equipend.repositories.ExpenseSplitsRepository;
import com.shahaam.equipend.repositories.ExpensesRepository;

@ExtendWith(MockitoExtension.class)
class ExpenseSplitServiceTest {

    @Mock
    private ExpenseSplitsRepository expSplitRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private ExpensesRepository expRepo;

    @InjectMocks
    private ExpenseSplitService expenseSplitService;

    private UUID userId;
    private UUID expenseId;
    private BigDecimal shareAmount;
    private ExpenseSplitRequestDTO requestDTO;
    private User mockUser;
    private Expense mockExpense;
    private ExpenseSplit mockExpenseSplit;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        expenseId = UUID.randomUUID();
        shareAmount = new BigDecimal("50.00");

        requestDTO = new ExpenseSplitRequestDTO(userId, expenseId, shareAmount);

        mockUser = new User();
        mockUser.setUserID(userId);

        mockExpense = new Expense();
        mockExpense.setExpenseID(expenseId);

        mockExpenseSplit = new ExpenseSplit(mockExpense, mockUser, shareAmount);
    }

    // --- ADD TESTS ---

    @Test
    @DisplayName("Should add expense split successfully when valid request is provided")
    void add_Success() {
        // Arrange
        when(expSplitRepo.existsByExpense_ExpenseIDAndUser_UserID(expenseId, userId)).thenReturn(false);
        when(userRepo.existsById(userId)).thenReturn(true);
        when(expRepo.existsById(expenseId)).thenReturn(true);
        
        // Mocking for the mapFromExpenseSplitRequestDTO call inside add()
        when(userRepo.findById(userId)).thenReturn(Optional.of(mockUser));
        when(expRepo.findById(expenseId)).thenReturn(Optional.of(mockExpense));
        when(expSplitRepo.save(any(ExpenseSplit.class))).thenReturn(mockExpenseSplit);

        // Act
        ExpenseSplitResponseDTO response = expenseSplitService.add(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(userId, response.user());
        assertEquals(expenseId, response.expense());
        assertEquals(shareAmount, response.shareAmount());

        verify(expSplitRepo).existsByExpense_ExpenseIDAndUser_UserID(expenseId, userId);
        verify(userRepo).existsById(userId);
        verify(expRepo).existsById(expenseId);
        verify(expSplitRepo).save(any(ExpenseSplit.class));
    }

    @Test
    @DisplayName("Should throw UniqueIdentifierAlreaydExistsException when split already exists")
    void add_ThrowsWhenSplitAlreadyExists() {
        // Arrange
        when(expSplitRepo.existsByExpense_ExpenseIDAndUser_UserID(expenseId, userId)).thenReturn(true);

        // Act & Assert
        UniqueIdentifierAlreaydExistsException exception = assertThrows(
                UniqueIdentifierAlreaydExistsException.class,
                () -> expenseSplitService.add(requestDTO)
        );

        assertNotNull(exception);
        verify(expSplitRepo).existsByExpense_ExpenseIDAndUser_UserID(expenseId, userId);
        verifyNoMoreInteractions(userRepo, expRepo);
        verify(expSplitRepo, never()).save(any());
    }

    @Test
    @DisplayName("Should throw RecordNotFoundException when user does not exist during add")
    void add_ThrowsWhenUserNotFound() {
        // Arrange
        when(expSplitRepo.existsByExpense_ExpenseIDAndUser_UserID(expenseId, userId)).thenReturn(false);
        when(userRepo.existsById(userId)).thenReturn(false);

        // Act & Assert
        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> expenseSplitService.add(requestDTO)
        );

        assertNotNull(exception);
        verify(userRepo).existsById(userId);
        verify(expRepo, never()).existsById(any());
        verify(expSplitRepo, never()).save(any());
    }

    @Test
    @DisplayName("Should throw RecordNotFoundException when expense does not exist during add")
    void add_ThrowsWhenExpenseNotFound() {
        // Arrange
        when(expSplitRepo.existsByExpense_ExpenseIDAndUser_UserID(expenseId, userId)).thenReturn(false);
        when(userRepo.existsById(userId)).thenReturn(true);
        when(expRepo.existsById(expenseId)).thenReturn(false);

        // Act & Assert
        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> expenseSplitService.add(requestDTO)
        );

        assertNotNull(exception);
        verify(expRepo).existsById(expenseId);
        verify(expSplitRepo, never()).save(any());
    }

    // --- GET (SINGLE) TESTS ---

    @Test
    @DisplayName("Should return ExpenseSplitResponseDTO when valid IDs are provided")
    void get_Success() {
        // Arrange
        ExpenseSplitID splitID = new ExpenseSplitID(expenseId, userId);
        when(expSplitRepo.findById(splitID)).thenReturn(Optional.of(mockExpenseSplit));

        // Act
        ExpenseSplitResponseDTO response = expenseSplitService.get(expenseId, userId);

        // Assert
        assertNotNull(response);
        assertEquals(userId, response.user());
        assertEquals(expenseId, response.expense());
        assertEquals(shareAmount, response.shareAmount());
        verify(expSplitRepo).findById(splitID);
    }

    @Test
    @DisplayName("Should throw RecordNotFoundException when split is not found by composite ID")
    void get_ThrowsWhenNotFound() {
        // Arrange
        ExpenseSplitID splitID = new ExpenseSplitID(expenseId, userId);
        when(expSplitRepo.findById(splitID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> expenseSplitService.get(expenseId, userId)
        );
        verify(expSplitRepo).findById(splitID);
    }

    // --- GET BY EXPENSE TESTS ---

    @Test
    @DisplayName("Should return list of splits when getByExpense is called")
    void getByExpense_Success() {
        // Arrange
        when(expSplitRepo.findByExpense_ExpenseID(expenseId)).thenReturn(List.of(mockExpenseSplit));

        // Act
        List<ExpenseSplitResponseDTO> responses = expenseSplitService.getByExpense(expenseId);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(expenseId, responses.get(0).expense());
        verify(expSplitRepo).findByExpense_ExpenseID(expenseId);
    }

    // --- GET BY USER TESTS ---

    @Test
    @DisplayName("Should return list of splits when getByUser is called")
    void getByUser_Success() {
        // Arrange
        when(expSplitRepo.findByUser_UserID(userId)).thenReturn(List.of(mockExpenseSplit));

        // Act
        List<ExpenseSplitResponseDTO> responses = expenseSplitService.getByUser(userId);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(userId, responses.get(0).user());
        verify(expSplitRepo).findByUser_UserID(userId);
    }
}