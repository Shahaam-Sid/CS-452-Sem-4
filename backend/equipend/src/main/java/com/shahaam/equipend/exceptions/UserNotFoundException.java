package com.shahaam.equipend.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }
}
// This method is specifically for Auth related stuff
// in other cases use RecordNotFoundException
