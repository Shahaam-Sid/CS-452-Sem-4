package com.shahaam.equipend.exceptions;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String field, String identifier) {
        super(field + ": " + identifier + " Not Found");
    }

    public RecordNotFoundException(String field, String identifierOne, String identifierTwo) {
        super(field + ": " + identifierOne +  " | " + identifierTwo + " Not Found");
    }
}
