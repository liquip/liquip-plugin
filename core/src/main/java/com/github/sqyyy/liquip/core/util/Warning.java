package com.github.sqyyy.liquip.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public interface Warning {
    @Nullable Cause getCause();

    void print(@NotNull Logger logger, @NotNull Object... args);

    @NotNull String getMessage(@NotNull Object... args);
}
