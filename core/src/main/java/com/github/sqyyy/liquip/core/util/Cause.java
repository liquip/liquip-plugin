package com.github.sqyyy.liquip.core.util;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Deprecated(forRemoval = true)
public class Cause {
    private final Error error;
    private final List<Warning> warnings;

    public Cause(@NotNull Error error, @NotNull List<@NotNull Warning> warnings) {
        this.error = error;
        this.warnings = warnings;
    }

    public Cause(@NotNull Status<?> status) {
        this.error = status.unwrapErr();
        this.warnings = status.getWarnings();
    }

    public @NotNull Error getError() {
        return error;
    }

    public @NotNull List<@NotNull Warning> getWarnings() {
        return warnings;
    }
}
