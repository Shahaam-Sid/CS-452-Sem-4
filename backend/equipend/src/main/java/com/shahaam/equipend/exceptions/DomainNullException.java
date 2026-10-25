package com.shahaam.equipend.exceptions;

public class DomainNullException extends RuntimeException {
    public DomainNullException(String domain) {
        super(domain + " cannot be null or empty");
    }
}
