package com.shahaam.equipend.utils;

import java.time.Instant;

import com.shahaam.equipend.exceptions.InvalidEmailException;
import com.shahaam.equipend.exceptions.InvalidPasswordException;
import com.shahaam.equipend.exceptions.InvalidTimeException;

public class ValidationsMethods {
    public static void checkNullObject(Object obj, String field) {
        if (obj == null) throw new IllegalArgumentException(field + " cannot be null");
    }
    public static void checkNullString(String string, String field) throws IllegalArgumentException {
        if (string == null || string.isEmpty() || string.isBlank())
            throw new IllegalArgumentException(field + " cannot be null or empty");
    }
    public static void checkLengthString(String string, int lengthMin, int lengthMax, String field) throws IllegalArgumentException {
        if (string.length() < lengthMin || string.length() > lengthMax)
            throw new IllegalArgumentException("Allowed Length for " + field + ": " + lengthMin + " - " + lengthMax);
    }
    public static void validateEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$";
        
        if (!email.matches(regex))
            throw new InvalidEmailException();
    }
    public static void validatePassword(String password) {

        if (password == null || password.length() < 8)
            throw new InvalidPasswordException("Password must be at least 8 characters");
        if (password.length() > 128)
            throw new InvalidPasswordException("Password cannot be more then 128 length");

        boolean hasUpper   = false;
        boolean hasLower   = false;
        boolean hasDigit   = false;
        boolean hasSpecial = false;

        String specialChars = "!@#$%^&*()-_=+[]{}|;:',.<>?/`~";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c))           hasUpper   = true;
            else if (Character.isLowerCase(c))      hasLower   = true;
            else if (Character.isDigit(c))          hasDigit   = true;
            else if (specialChars.indexOf(c) >= 0)  hasSpecial = true;
        }

        if (!hasUpper)   throw new InvalidPasswordException("Password must contain at least one uppercase letter");
        if (!hasLower)   throw new InvalidPasswordException("Password must contain at least one lowercase letter");
        if (!hasDigit)   throw new InvalidPasswordException("Password must contain at least one digit");
        if (!hasSpecial) throw new InvalidPasswordException("Password must contain at least one special character");
    }

    public static void isStringAplhaOnly(String str, String field) {
        for (char c : str.toUpperCase().toCharArray()) {
            
            if ((c < 'A' || c > 'Z') && c != ' ') 
                throw new IllegalArgumentException(field + " must only contain Alphabets");
        }
    }
    public static void isAlphaNumericUnderscoreDot(String str, String field) {
        if (!str.matches("^[a-zA-Z0-9_.]+$"))
            throw new IllegalArgumentException(field + " must only contain AlphaNumric Characters, '_' or '.'");
    }

    public static void checkTimeStamp(Instant time, Instant checkAgainst, String msg) {
        if (time.isAfter(checkAgainst))
            throw new InvalidTimeException(msg);
    }
}