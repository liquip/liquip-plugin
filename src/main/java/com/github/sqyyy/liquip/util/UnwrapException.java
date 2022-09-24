package com.github.sqyyy.liquip.util;

public class UnwrapException extends RuntimeException {
    public UnwrapException() {
    }

    public UnwrapException(String message) {
        super(message);
    }

    public UnwrapException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnwrapException(Throwable cause) {
        super(cause);
    }

    public UnwrapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
