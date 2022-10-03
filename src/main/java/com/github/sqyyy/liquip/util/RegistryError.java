package com.github.sqyyy.liquip.util;

public enum RegistryError {
    INVALID_IDENTIFIER("The identifier is invalid");

    private final String message;

    RegistryError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
