package com.shahaam.equipend.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class UtilityMethodsTest {

    @Test
    void testCalculateEqualSplit_EvenDivision() {
        // Arrange
        UUID payer = UUID.randomUUID();
        UUID participant2 = UUID.randomUUID();
        UUID participant3 = UUID.randomUUID();
        List<UUID> participants = Arrays.asList(payer, participant2, participant3);
        BigDecimal totalAmount = new BigDecimal("90.00");

        // Act
        Map<UUID, BigDecimal> result = UtilityMethods.calculateEqualSplit(totalAmount, payer, participants);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(new BigDecimal("30.00"), result.get(payer));
        assertEquals(new BigDecimal("30.00"), result.get(participant2));
        assertEquals(new BigDecimal("30.00"), result.get(participant3));
    }

    @Test
    void testCalculateEqualSplit_WithRemainderAssignedToPayer() {
        // Arrange
        UUID payer = UUID.randomUUID();
        UUID participant2 = UUID.randomUUID();
        UUID participant3 = UUID.randomUUID();
        List<UUID> participants = Arrays.asList(payer, participant2, participant3);
        BigDecimal totalAmount = new BigDecimal("10.00"); // 10.00 / 3 = 3.33 base, remainder 0.01

        // Act
        Map<UUID, BigDecimal> result = UtilityMethods.calculateEqualSplit(totalAmount, payer, participants);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        // Payer gets base share (3.33) + remainder (0.01) = 3.34
        assertEquals(new BigDecimal("3.34"), result.get(payer));
        assertEquals(new BigDecimal("3.33"), result.get(participant2));
        assertEquals(new BigDecimal("3.33"), result.get(participant3));
    }

    @Test
    void testCalculateEqualSplit_NullParticipants_ThrowsException() {
        UUID payer = UUID.randomUUID();
        BigDecimal totalAmount = new BigDecimal("50.00");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> UtilityMethods.calculateEqualSplit(totalAmount, payer, null)
        );

        assertEquals("Participant list cannot be empty.", exception.getMessage());
    }

    @Test
    void testCalculateEqualSplit_EmptyParticipants_ThrowsException() {
        UUID payer = UUID.randomUUID();
        BigDecimal totalAmount = new BigDecimal("50.00");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> UtilityMethods.calculateEqualSplit(totalAmount, payer, Collections.emptyList())
        );

        assertEquals("Participant list cannot be empty.", exception.getMessage());
    }

    @Test
    void testCalculateEqualSplit_PayerNotInParticipants_ThrowsException() {
        UUID payer = UUID.randomUUID();
        UUID participant1 = UUID.randomUUID();
        UUID participant2 = UUID.randomUUID();
        List<UUID> participants = Arrays.asList(participant1, participant2);
        BigDecimal totalAmount = new BigDecimal("20.00");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> UtilityMethods.calculateEqualSplit(totalAmount, payer, participants)
        );

        assertEquals("Payer must be included in the participants list.", exception.getMessage());
    }

    @Test
    void testCalculateEqualSplit_SingleParticipant() {
        UUID payer = UUID.randomUUID();
        List<UUID> participants = Collections.singletonList(payer);
        BigDecimal totalAmount = new BigDecimal("45.50");

        Map<UUID, BigDecimal> result = UtilityMethods.calculateEqualSplit(totalAmount, payer, participants);

        assertNotNull(result);
        assertEquals(1, result.size());
        // 45.50 / 1 = 45.50 base, remainder 0.00
        assertEquals(new BigDecimal("45.50"), result.get(payer));
    }

    @Test
    void testCalculateEqualSplit_ZeroAmount() {
        UUID payer = UUID.randomUUID();
        UUID participant2 = UUID.randomUUID();
        List<UUID> participants = Arrays.asList(payer, participant2);
        BigDecimal totalAmount = BigDecimal.ZERO;

        Map<UUID, BigDecimal> result = UtilityMethods.calculateEqualSplit(totalAmount, payer, participants);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(new BigDecimal("0.00"), result.get(payer));
        assertEquals(new BigDecimal("0.00"), result.get(participant2));
    }

    @Test
    void testCalculatePercentageSplit_ExactDivision() {
        // Arrange
        UUID p1 = UUID.randomUUID();
        UUID p2 = UUID.randomUUID();
        
        Map<UUID, BigDecimal> percentages = new HashMap<>();
        percentages.put(p1, new BigDecimal("50.00"));
        percentages.put(p2, new BigDecimal("50.00"));
        
        BigDecimal totalAmount = new BigDecimal("100.00");

        // Act
        Map<UUID, BigDecimal> result = UtilityMethods.calculatePercentageSplit(totalAmount, percentages);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(new BigDecimal("50.00"), result.get(p1));
        assertEquals(new BigDecimal("50.00"), result.get(p2));
    }

    @Test
    void testCalculatePercentageSplit_WithPositiveRemainderAdjustment() {
        // Arrange
        UUID p1 = UUID.randomUUID();
        UUID p2 = UUID.randomUUID();
        UUID p3 = UUID.randomUUID();
        
        Map<UUID, BigDecimal> percentages = new HashMap<>();
        percentages.put(p1, new BigDecimal("33.33"));
        percentages.put(p2, new BigDecimal("33.33"));
        percentages.put(p3, new BigDecimal("33.34"));
        
        // 100.00 * 33.33 / 100 = 33.33 (for p1 and p2)
        // 100.00 * 33.34 / 100 = 33.34 (for p3)
        // Sum distributed = 99.99, Remainder = +0.01, which goes to the first processed participant (p1)
        BigDecimal totalAmount = new BigDecimal("100.00");

        // Act
        Map<UUID, BigDecimal> result = UtilityMethods.calculatePercentageSplit(totalAmount, percentages);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        // p1 gets base share (33.33) + remainder (0.01) = 33.34
        // Depending on iteration order, p1 could be any of them, but we check values sum up correctly to total
        BigDecimal sumResult = result.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(totalAmount, sumResult);
    }

    @Test
    void testCalculatePercentageSplit_WithNegativeRemainderAdjustment() {
        // Arrange
        UUID p1 = UUID.randomUUID();
        UUID p2 = UUID.randomUUID();
        UUID p3 = UUID.randomUUID();
        
        Map<UUID, BigDecimal> percentages = new HashMap<>();
        percentages.put(p1, new BigDecimal("33.34"));
        percentages.put(p2, new BigDecimal("33.33"));
        percentages.put(p3, new BigDecimal("33.33"));
        
        BigDecimal totalAmount = new BigDecimal("10.00"); // Creates rounding adjustments

        // Act
        Map<UUID, BigDecimal> result = UtilityMethods.calculatePercentageSplit(totalAmount, percentages);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        
        // Verify total sum matches original total amount exactly
        BigDecimal sumResult = result.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(0, totalAmount.compareTo(sumResult));
    }

    @Test
    void testCalculatePercentageSplit_NullMap_ThrowsException() {
        BigDecimal totalAmount = new BigDecimal("50.00");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> UtilityMethods.calculatePercentageSplit(totalAmount, null)
        );

        assertEquals("Participant percentages cannot be empty.", exception.getMessage());
    }

    @Test
    void testCalculatePercentageSplit_EmptyMap_ThrowsException() {
        BigDecimal totalAmount = new BigDecimal("50.00");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> UtilityMethods.calculatePercentageSplit(totalAmount, Collections.emptyMap())
        );

        assertEquals("Participant percentages cannot be empty.", exception.getMessage());
    }

    @Test
    void testCalculatePercentageSplit_PercentagesLessThan100_ThrowsException() {
        UUID p1 = UUID.randomUUID();
        UUID p2 = UUID.randomUUID();
        
        Map<UUID, BigDecimal> percentages = new HashMap<>();
        percentages.put(p1, new BigDecimal("40.00"));
        percentages.put(p2, new BigDecimal("50.00")); // Sum = 90.00%
        
        BigDecimal totalAmount = new BigDecimal("100.00");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> UtilityMethods.calculatePercentageSplit(totalAmount, percentages)
        );

        assertTrue(exception.getMessage().contains("Percentages must sum up to exactly 100%"));
    }

    @Test
    void testCalculatePercentageSplit_PercentagesGreaterThan100_ThrowsException() {
        UUID p1 = UUID.randomUUID();
        UUID p2 = UUID.randomUUID();
        
        Map<UUID, BigDecimal> percentages = new HashMap<>();
        percentages.put(p1, new BigDecimal("60.00"));
        percentages.put(p2, new BigDecimal("50.00")); // Sum = 110.00%
        
        BigDecimal totalAmount = new BigDecimal("100.00");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> UtilityMethods.calculatePercentageSplit(totalAmount, percentages)
        );

        assertTrue(exception.getMessage().contains("Percentages must sum up to exactly 100%"));
    }

    @Test
    void testCalculatePercentageSplit_SingleParticipant100Percent() {
        UUID p1 = UUID.randomUUID();
        
        Map<UUID, BigDecimal> percentages = new HashMap<>();
        percentages.put(p1, new BigDecimal("100.00"));
        
        BigDecimal totalAmount = new BigDecimal("75.50");

        Map<UUID, BigDecimal> result = UtilityMethods.calculatePercentageSplit(totalAmount, percentages);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("75.50"), result.get(p1));
    }

    @Test
    void testCalculatePercentageSplit_ZeroAmount() {
        UUID p1 = UUID.randomUUID();
        UUID p2 = UUID.randomUUID();
        
        Map<UUID, BigDecimal> percentages = new HashMap<>();
        percentages.put(p1, new BigDecimal("40.00"));
        percentages.put(p2, new BigDecimal("60.00"));
        
        BigDecimal totalAmount = BigDecimal.ZERO;

        Map<UUID, BigDecimal> result = UtilityMethods.calculatePercentageSplit(totalAmount, percentages);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(new BigDecimal("0.00"), result.get(p1));
        assertEquals(new BigDecimal("0.00"), result.get(p2));
    }
}
