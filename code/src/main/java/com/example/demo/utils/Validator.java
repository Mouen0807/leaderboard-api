package com.example.demo.utils;

import java.util.UUID;

public class Validator {

    public static boolean isValidUUID(String uuidStr) {
        try {
            UUID uuid = UUID.fromString(uuidStr);  // Try parsing the string to a UUID
            return true;  // If no exception is thrown, it's valid
        } catch (IllegalArgumentException e) {
            return false;  // If an exception is thrown, it's not valid
        }
    }
}
