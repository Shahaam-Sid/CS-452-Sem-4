package com.shahaam.equipend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
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
import com.shahaam.equipend.dto.ExpenseRequestDTO;
import com.shahaam.equipend.dto.ExpenseResponseDTO;
import com.shahaam.equipend.enums.SplitType;
import com.shahaam.equipend.exceptions.MemberNotInGroupException;
import com.shahaam.equipend.exceptions.RecordNotFoundException;
import com.shahaam.equipend.models.Expense;
import com.shahaam.equipend.models.Group;
import com.shahaam.equipend.repositories.ExpenseSplitsRepository;
import com.shahaam.equipend.repositories.ExpensesRepository;
import com.shahaam.equipend.repositories.GroupMembersRepository;
import com.shahaam.equipend.repositories.GroupsRepository;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpensesRepository expenseRepo;
    @Mock
    private GroupsRepository groupsRepo;
    @Mock
    private UserRepository userRepo;
    @Mock
    private GroupMembersRepository grpMemRepo;
    @Mock
    private ExpenseSplitsRepository expSplitRepo;

    @InjectMocks
    private ExpenseService expenseService;

    private UUID userId1;
    private UUID userId2;
    private UUID groupId;
    private UUID expenseId;
    private User user1;
    private User user2;
    private Group group;
    private Expense expense;

    @BeforeEach
    void setUp() {
        userId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();
        groupId = UUID.randomUUID();
        expenseId = UUID.randomUUID();

        // Ensure IDs are explicitly set so mapping DTOs doesn't return null
        user1 = new User();
        user1.setUserID(userId1);

        user2 = new User();
        user2.setUserID(userId2);

        group = new Group();
        group.setGroupID(groupId);

        expense = new Expense("Dinner", new BigDecimal("100.00"), group, user1, user2, SplitType.EQUAL);
        expense.setExpenseID(expenseId);
        expense.setCreatedAt(Instant.now());
    }

    @Test
    void testCountGroupExpenses() {
        when(expenseRepo.countByGroup_GroupID(groupId)).thenReturn(5L);

        long count = expenseService.countGroupExpenses(groupId);

        assertEquals(5L, count);
        verify(expenseRepo, times(1)).countByGroup_GroupID(groupId);
    }

    @Test
    void testAddExpense_EqualSplit_Success() {
        ExpenseRequestDTO request = new ExpenseRequestDTO(
                "Dinner",
                new BigDecimal("100.00"),
                groupId,
                userId1,
                userId2,
                SplitType.EQUAL,
                List.of(userId1, userId2),
                Map.of(userId1, new BigDecimal("50.00"), userId2, new BigDecimal("50.00"))
        );

        when(userRepo.existsById(userId1)).thenReturn(true);
        when(userRepo.existsById(userId2)).thenReturn(true);
        when(groupsRepo.existsById(groupId)).thenReturn(true);

        when(groupsRepo.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepo.findById(userId1)).thenReturn(Optional.of(user1));
        when(userRepo.findById(userId2)).thenReturn(Optional.of(user2));
        
        when(expenseRepo.save(any(Expense.class))).thenReturn(expense);
        // CRITICAL: Stub findById for split mapping lookup
        when(expenseRepo.findById(any())).thenReturn(Optional.of(expense));
        
        when(grpMemRepo.existsByGroup_GroupIDAndUser_UserID(any(), any())).thenReturn(true);

        ExpenseResponseDTO response = expenseService.add(request);

        assertNotNull(response);
        assertEquals("Dinner", response.title());
        verify(expenseRepo, times(1)).save(any(Expense.class));
        verify(expenseRepo, times(1)).flush();
    }

    @Test
    void testAddExpense_UserNotFound_ThrowsException() {
        ExpenseRequestDTO request = new ExpenseRequestDTO(
                "Dinner", new BigDecimal("100.00"), groupId, userId1, userId2, SplitType.EQUAL, List.of(), Map.of()
        );

        when(userRepo.existsById(userId1)).thenReturn(false);

        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> expenseService.add(request)
        );

        assertTrue(exception.getMessage().contains("User"));
        verify(expenseRepo, never()).save(any());
    }

    @Test
    void testAddExpense_MemberNotInGroup_ThrowsException() {
        ExpenseRequestDTO request = new ExpenseRequestDTO(
                "Dinner",
                new BigDecimal("100.00"),
                groupId,
                userId1,
                userId2,
                SplitType.EQUAL,
                List.of(userId1, userId2),
                Map.of()
        );

        when(userRepo.existsById(userId1)).thenReturn(true);
        when(userRepo.existsById(userId2)).thenReturn(true);
        when(groupsRepo.existsById(groupId)).thenReturn(true);

        when(groupsRepo.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepo.findById(userId1)).thenReturn(Optional.of(user1));
        when(userRepo.findById(userId2)).thenReturn(Optional.of(user2));
        
        when(expenseRepo.save(any(Expense.class))).thenReturn(expense);
        
        // Simulate a participant who is NOT in the group
        when(grpMemRepo.existsByGroup_GroupIDAndUser_UserID(eq(groupId), any())).thenReturn(false);

        assertThrows(
                MemberNotInGroupException.class,
                () -> expenseService.add(request)
        );
    }

    @Test
    void testGetExpense_Success() {
        when(expenseRepo.findById(expenseId)).thenReturn(Optional.of(expense));

        ExpenseResponseDTO response = expenseService.get(expenseId);

        assertNotNull(response);
        assertEquals(expenseId, response.expenseID());
        verify(expenseRepo, times(1)).findById(expenseId);
    }

    @Test
    void testGetExpense_NotFound_ThrowsException() {
        when(expenseRepo.findById(expenseId)).thenReturn(Optional.empty());

        assertThrows(
                RecordNotFoundException.class,
                () -> expenseService.get(expenseId)
        );
    }

    @Test
    void testGetGroupExpenses() {
        when(expenseRepo.findByGroup_GroupID(groupId)).thenReturn(List.of(expense));

        List<ExpenseResponseDTO> expenses = expenseService.getGroupExpenses(groupId);

        assertEquals(1, expenses.size());
        assertEquals("Dinner", expenses.get(0).title());
    }

    @Test
    void testGetExpensesByCreator() {
        when(expenseRepo.findByCreatedBy_UserID(userId1)).thenReturn(List.of(expense));

        List<ExpenseResponseDTO> expenses = expenseService.getExpensesByCreator(userId1);

        assertEquals(1, expenses.size());
        assertEquals(userId1, expenses.get(0).createdBy());
    }

    @Test
    void testGetExpensesByPayer() {
        when(expenseRepo.findByPaidBy_UserID(userId2)).thenReturn(List.of(expense));

        List<ExpenseResponseDTO> expenses = expenseService.getExpensesByPayer(userId2);

        assertEquals(1, expenses.size());
        assertEquals(userId2, expenses.get(0).paidBy());
    }
}