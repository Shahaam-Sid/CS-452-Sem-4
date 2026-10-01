package com.shahaam.equipend.exceptions;

public class InvalidDOBException extends RuntimeException {
    public InvalidDOBException(String reason) {
        super("Invalid Age: " + reason);
    }
}
