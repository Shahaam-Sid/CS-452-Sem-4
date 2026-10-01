package com.shahaam.equipend.exceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() {
        super("Invalid Email Format");
    } 
    
}
