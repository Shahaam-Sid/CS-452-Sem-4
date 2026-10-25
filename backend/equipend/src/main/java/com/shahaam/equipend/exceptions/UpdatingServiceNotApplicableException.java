package com.shahaam.equipend.exceptions;

public class UpdatingServiceNotApplicableException extends RuntimeException {
    public UpdatingServiceNotApplicableException(String field) {
        super("Updating service not applicable for " + field);
    }
}
