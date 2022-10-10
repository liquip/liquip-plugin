package com.github.sqyyy.liquip.core.util;

public enum UtilError {
    INVALID_IDENTIFIER("The identifier is invalid");

    private final String message;

    UtilError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
