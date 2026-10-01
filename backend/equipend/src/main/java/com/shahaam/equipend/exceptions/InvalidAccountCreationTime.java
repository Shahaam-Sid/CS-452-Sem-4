package com.shahaam.equipend.exceptions;

public class InvalidAccountCreationTime extends RuntimeException {
    public InvalidAccountCreationTime(String reason) {
        super("Invalid Account Creation Time: " + reason);
    }
}
