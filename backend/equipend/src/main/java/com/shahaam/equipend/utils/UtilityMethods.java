package com.shahaam.equipend.utils;

import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;

public class UtilityMethods {
    
    public static UUID generateUUIDv7() {
        return  UuidCreator.getTimeOrderedEpoch();
    }
}
