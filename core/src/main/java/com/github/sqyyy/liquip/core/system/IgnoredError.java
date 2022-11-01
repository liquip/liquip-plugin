package com.github.sqyyy.liquip.core.system;

import com.github.sqyyy.liquip.core.util.Cause;
import com.github.sqyyy.liquip.core.util.Error;
import com.github.sqyyy.liquip.core.util.Warning;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

@Deprecated(forRemoval = true)
public class IgnoredError implements Warning {
    private final Error error;
    private final String message;

    public IgnoredError(@Nullable String message, @NotNull Error error) {
        this.message = message;
        this.error = error;
    }

    public IgnoredError(@NotNull Error error) {
        message = null;
        this.error = error;
    }

    @Override
    public @Nullable Cause getCause() {
        return error.getCause();
    }

    @Override
    public void print(@NotNull Logger logger, @NotNull Object... args) {
        if (message != null) {
            logger.warn(message);
        }
        final String errorMessage = error.getMessage(args);
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
    public @NotNull String getMessage(@NotNull Object... args) {
        return error.getMessage(args);
    }
}
