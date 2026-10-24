package com.shahaam.equipend.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;

public class UtilityMethods {
    
    public static UUID generateUUIDv7() {
        return  UuidCreator.getTimeOrderedEpoch();
    }

    public static Map<UUID, BigDecimal> calculateEqualSplit(
            BigDecimal totalAmount, 
            UUID payer, 
            List<UUID> participants) {
        
        if (participants == null || participants.isEmpty()) {
            throw new IllegalArgumentException("Participant list cannot be empty.");
        }

        if (!participants.contains(payer)) {
            throw new IllegalArgumentException("Payer must be included in the participants list.");
        }

        int totalParticipants = participants.size();
        Map<UUID, BigDecimal> shares = new HashMap<>();

        // 1. Compute each participant's base share rounded down/half-even to 2 decimal places
        BigDecimal baseShare = totalAmount.divide(
                BigDecimal.valueOf(totalParticipants), 
                2, 
                RoundingMode.DOWN
        );

        // Assign the base share to everyone initially
        for (UUID participant : participants) {
            shares.put(participant, baseShare);
        }

        // 2. Calculate the sum of all distributed base shares
        BigDecimal distributedSum = baseShare.multiply(BigDecimal.valueOf(totalParticipants));

        // 3. Compute the residual remainder (Total - Distributed Sum)
        BigDecimal remainder = totalAmount.subtract(distributedSum);

        // 4. If there is a remainder (e.g., 0.01), assign it to the payer
        if (remainder.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal payerShare = shares.get(payer);
            shares.put(payer, payerShare.add(remainder));
        }

        return shares;
    }

    public static Map<UUID, BigDecimal> calculatePercentageSplit(
            BigDecimal totalAmount, 
            Map<UUID, BigDecimal> participantPercentages) {
        
        if (participantPercentages == null || participantPercentages.isEmpty()) {
            throw new IllegalArgumentException("Participant percentages cannot be empty.");
        }

        // 1. Verify that the percentages sum up to 100%
        BigDecimal totalPercentage = BigDecimal.ZERO;
        for (BigDecimal percentage : participantPercentages.values()) {
            totalPercentage = totalPercentage.add(percentage);
        }
        
        if (totalPercentage.compareTo(new BigDecimal("100.00")) != 0) {
            throw new IllegalArgumentException("Percentages must sum up to exactly 100%. Total was: " + totalPercentage);
        }

        Map<UUID, BigDecimal> shares = new HashMap<>();
        BigDecimal distributedSum = BigDecimal.ZERO;
        UUID firstParticipant = null;

        // 2. Compute each participant's share: (Total * Percentage) / 100
        for (Map.Entry<UUID, BigDecimal> entry : participantPercentages.entrySet()) {
            UUID participant = entry.getKey();
            BigDecimal percentage = entry.getValue();

            if (firstParticipant == null) {
                firstParticipant = participant; // Keep track of the first participant to catch the remainder
            }

            // Share = total * (percentage / 100), rounded to 2 decimal places
            BigDecimal share = totalAmount
                    .multiply(percentage)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            shares.put(participant, share);
            distributedSum = distributedSum.add(share);
        }

        // 3. Calculate any residual remainder due to rounding
        BigDecimal remainder = totalAmount.subtract(distributedSum);

        // 4. If there is a remainder (e.g., +0.01 or -0.01), adjust the first participant's share
        if (remainder.compareTo(BigDecimal.ZERO) != 0 && firstParticipant != null) {
            BigDecimal adjustedShare = shares.get(firstParticipant).add(remainder);
            shares.put(firstParticipant, adjustedShare);
        }

        return shares;
    }
}
