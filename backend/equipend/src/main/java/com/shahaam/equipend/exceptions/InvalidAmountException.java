package com.shahaam.equipend.exceptions;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(String reason) {
        super(reason);
    }
}
