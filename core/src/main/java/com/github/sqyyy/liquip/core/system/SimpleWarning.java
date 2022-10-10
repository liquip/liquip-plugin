package com.github.sqyyy.liquip.core.system;

import com.github.sqyyy.liquip.core.util.Cause;
import com.github.sqyyy.liquip.core.util.Warning;
import org.slf4j.Logger;

public class SimpleWarning implements Warning {
    private final String message;

    public SimpleWarning(String message) {
        this.message = message;
    }

    @Override
    public Cause getCause() {
        return null;
    }

    @Override
    public void print(Logger logger, Object... args) {
        logger.warn(message);
    }

    @Override
    public String getMessage(Object... args) {
        return message;
    }
}
