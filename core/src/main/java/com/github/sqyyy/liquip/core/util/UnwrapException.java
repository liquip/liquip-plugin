package com.github.sqyyy.liquip.core.util;

import org.jetbrains.annotations.Nullable;

public class UnwrapException extends RuntimeException {
    public UnwrapException() {
    }

    public UnwrapException(@Nullable String message) {
        super(message);
    }

    public UnwrapException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public UnwrapException(@Nullable Throwable cause) {
        super(cause);
    }

    public UnwrapException(@Nullable String message, @Nullable Throwable cause, boolean enableSuppression,
                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
