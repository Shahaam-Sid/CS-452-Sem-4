package com.shahaam.equipend.exceptions;

public class InvalidDomainException extends RuntimeException {
    public InvalidDomainException(String domain, String value) {
        super("Invalid " + domain + ": " + value);
    }
}
