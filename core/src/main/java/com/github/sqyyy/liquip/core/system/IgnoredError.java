package com.github.sqyyy.liquip.core.system;

import com.github.sqyyy.liquip.core.util.Cause;
import com.github.sqyyy.liquip.core.util.Error;
import com.github.sqyyy.liquip.core.util.Warning;
import org.slf4j.Logger;

public class IgnoredError implements Warning {
    private final Error error;
    private final String message;

    public IgnoredError(String message, Error error) {
        this.message = message;
        this.error = error;
    }

    public IgnoredError(Error error) {
        message = null;
        this.error = error;
    }

    @Override
    public Cause getCause() {
        return error.getCause();
    }

    @Override
    public void print(Logger logger, Object... args) {
        if (message != null) {
            logger.warn(message);
        }

        final var errorMessage = error.getMessage(args);

        if (message != null) {
            for (String line : errorMessage.split("\n")) {
                logger.warn("  " + line);
            }

            return;
        }
        for (String line : errorMessage.split("\n")) {
            logger.warn(line);
        }
    }

    @Override
    public String getMessage(Object... args) {
        return error.getMessage(args);
    }
}
