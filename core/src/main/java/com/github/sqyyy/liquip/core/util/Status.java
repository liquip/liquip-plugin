package com.github.sqyyy.liquip.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Deprecated(forRemoval = true)
public class Status<T> {
    private final List<Warning> warnings;
    private final boolean isFinal;
    private T value;
    private Error error;
    private boolean isOk;

    public Status() {
        this.value = null;
        this.error = null;
        this.warnings = new ArrayList<>(1);
        this.isOk = false;
        this.isFinal = false;
    }

    private Status(T value, Error error, List<Warning> warnings, boolean isOk) {
        this.value = value;
        this.error = error;
        this.warnings = warnings;
        this.isOk = isOk;
        isFinal = true;
    }

    public static <T> @NotNull Status<@NotNull T> ok(@NotNull T value) {
        return new Status<>(value, null, new ArrayList<>(1), true);
    }

    public static <T> @NotNull Status<@NotNull T> ok(@NotNull T value, @NotNull List<@NotNull Warning> warnings) {
        return new Status<>(value, null, warnings, true);
    }

    public static <T> @NotNull Status<@NotNull T> err(@NotNull Error error) {
        return new Status<>(null, error, new ArrayList<>(1), false);
    }

    public static <T> @NotNull Status<@NotNull T> err(@NotNull Error error, @NotNull List<@NotNull Warning> warnings) {
        return new Status<>(null, error, warnings, false);
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        if (isFinal) {
            return;
        }
        isOk = ok;
    }

    public boolean hasWarning() {
        return !warnings.isEmpty();
    }

    public boolean isErr() {
        return !isOk;
    }

    public @NotNull Optional<@NotNull T> ok() {
        return isOk ? Optional.of(value) : Optional.empty();
    }

    public @NotNull Optional<@NotNull Error> err() {
        return isOk ? Optional.empty() : Optional.of(error);
    }

    public @NotNull T expect(String message) {
        if (!isOk) {
            throw new UnwrapException(message);
        }
        return value;
    }

    public @NotNull T unwrap() {
        if (!isOk) {
            throw new UnwrapException("Status was not ok");
        }
        return value;
    }

    public @NotNull Error expectErr(String message) {
        if (isOk) {
            throw new UnwrapException(message);
        }
        return error;
    }

    public @NotNull Error unwrapErr() {
        if (isOk) {
            throw new UnwrapException("Status was ok");
        }
        return error;
    }

    public @NotNull T unwrapOr(@NotNull T defaultValue) {
        return isOk ? value : defaultValue;
    }

    public @NotNull List<@NotNull Warning> getWarnings() {
        return List.copyOf(warnings);
    }

    public void setValue(@Nullable T value) {
        if (isFinal) {
            return;
        }
        isOk = true;
        this.value = value;
    }

    public void setError(@Nullable Error error) {
        if (isFinal) {
            return;
        }
        isOk = false;
        this.error = error;
    }

    public void addWarning(@NotNull Warning warning) {
        if (isFinal) {
            return;
        }
        warnings.add(warning);
    }

    public void addWarnings(@NotNull Collection<@NotNull Warning> warnings) {
        if (isFinal) {
            return;
        }
        this.warnings.addAll(warnings);
    }
}
