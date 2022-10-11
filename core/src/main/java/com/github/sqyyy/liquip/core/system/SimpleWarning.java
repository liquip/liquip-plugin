package com.github.sqyyy.liquip.core.system;

import com.github.sqyyy.liquip.core.util.Cause;
import com.github.sqyyy.liquip.core.util.Warning;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class SimpleWarning implements Warning {
    private final String message;

    public SimpleWarning(@NotNull String message) {
        this.message = message;
    }

    @Override
    public @Nullable Cause getCause() {
        return null;
    }

    @Override
    public void print(@NotNull Logger logger, @NotNull Object... args) {
        logger.warn(message);
    }

    @Override
    public @NotNull String getMessage(@NotNull Object... args) {
        return message;
    }
}
