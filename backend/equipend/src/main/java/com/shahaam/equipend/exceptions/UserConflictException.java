package com.shahaam.equipend.exceptions;

public class UserConflictException extends RuntimeException {
    public UserConflictException() {
        super("Payer and payee cannot be same");
    }
    public  UserConflictException(String msg) {
        super(msg);
    }
}
