package com.github.sqyyy.liquip.core.util;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Deprecated
public class Result<T, E> {
    private final T value;
    private final E error;
    private final boolean ok;

    private Result(T value, E error, boolean isOk) {
        this.value = value;
        this.error = error;
        this.ok = isOk;
    }

    public static <T, E> @NotNull Result<@NotNull T, @NotNull E> ok(@NotNull T value) {
        return new Result<>(value, null, true);
    }

    public static <T, E> @NotNull Result<@NotNull T, @NotNull E> err(@NotNull E error) {
        return new Result<>(null, error, false);
    }

    public boolean isOk() {
        return ok;
    }

    public boolean isErr() {
        return !ok;
    }

    public @NotNull Optional<@NotNull T> ok() {
        return ok ? Optional.of(value) : Optional.empty();
    }

    public @NotNull Optional<@NotNull E> err() {
        return ok ? Optional.empty() : Optional.of(error);
    }

    public @NotNull T expect(String msg) {
        if (!ok) {
            throw new UnwrapException(msg);
        }
        return value;
    }

    public @NotNull T unwrap() {
        if (!ok) {
            throw new UnwrapException("Result was not ok");
        }
        return value;
    }

    public @NotNull E expectErr(String msg) {
        if (ok) {
            throw new UnwrapException(msg);
        }
        return error;
    }

    public @NotNull E unwrapErr() {
        if (ok) {
            throw new UnwrapException("Result was ok");
        }
        return error;
    }

    public @NotNull T unwrapOr(T defaultValue) {
        return ok ? value : defaultValue;
    }
}
