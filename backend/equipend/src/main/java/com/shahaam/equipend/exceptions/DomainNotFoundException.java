package com.shahaam.equipend.exceptions;

public class DomainNotFoundException extends RuntimeException {
    public DomainNotFoundException(String domain) {
        super(domain + " not found");
    }
}
