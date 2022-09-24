package com.github.sqyyy.liquip.util;

import java.util.Optional;

public class Result<T, E> {
    private final T value;
    private final E error;
    private final boolean ok;

    private Result(T value, E error, boolean isOk) {
        this.value = value;
        this.error = error;
        this.ok = isOk;
    }

    public static <T, E> Result<T, E> ok(T value) {
        return new Result<>(value, null, true);
    }

    public static <T, E> Result<T, E> err(E error) {
        return new Result<>(null, error, false);
    }

    public boolean isOk() {
        return ok;
    }

    public boolean isErr() {
        return !ok;
    }

    public Optional<T> ok() {
        return ok ? Optional.of(value) : Optional.empty();
    }

    public Optional<E> err() {
        return ok ? Optional.empty() : Optional.of(error);
    }

    public T expect(String msg) {
        if (!ok) {
            throw new UnwrapException(msg);
        }
        return value;
    }

    public T unwrap() {
        if (!ok) {
            throw new UnwrapException("Result was not ok");
        }
        return value;
    }

    public E expectErr(String msg) {
        if (ok) {
            throw new UnwrapException(msg);
        }
        return error;
    }

    public E unwrapErr() {
        if (ok) {
            throw new UnwrapException("Result was ok");
        }
        return error;
    }

    public T unwrapOr(T defaultValue) {
        return ok ? value : defaultValue;
    }
}
