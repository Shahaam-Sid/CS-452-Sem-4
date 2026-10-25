package com.shahaam.equipend.exceptions;


public class MemberNotInGroupException extends RuntimeException {
    public MemberNotInGroupException(String userID, String groupID) {
        super("User: " + userID + " not in Group: " + groupID);
    }

}
