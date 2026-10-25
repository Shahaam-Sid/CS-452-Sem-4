package com.shahaam.equipend.exceptions;

public class UniqueIdentifierAlreaydExistsException extends RuntimeException {
    public UniqueIdentifierAlreaydExistsException(String field) {
        super(field + " already exist");
    }
}
