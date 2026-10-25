package com.shahaam.equipend.exceptions;

public class InvalidTimeException extends RuntimeException {
    public InvalidTimeException(String msg) {
        super("Invalid Time: " + msg);
    }
}
