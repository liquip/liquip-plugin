package com.github.sqyyy.liquip.core.util;

import org.jetbrains.annotations.NotNull;

public enum UtilError {
    INVALID_IDENTIFIER("The identifier is invalid");

    private final String message;

    UtilError(@NotNull String message) {
        this.message = message;
    }

    public @NotNull String getMessage() {
        return message;
    }
}
