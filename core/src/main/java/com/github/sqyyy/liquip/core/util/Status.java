package com.github.sqyyy.liquip.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    public static <T> Status<T> ok(T value) {
        return new Status<>(value, null, new ArrayList<>(1), true);
    }

    public static <T> Status<T> ok(T value, List<Warning> warnings) {
        return new Status<>(value, null, warnings, true);
    }

    public static <T> Status<T> err(Error error) {
        return new Status<>(null, error, new ArrayList<>(1), false);
    }

    public static <T> Status<T> err(Error error, List<Warning> warnings) {
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

    public Optional<T> ok() {
        return isOk ? Optional.of(value) : Optional.empty();
    }

    public Optional<Error> err() {
        return isOk ? Optional.empty() : Optional.of(error);
    }

    public T expect(String message) {
        if (!isOk) {
            throw new UnwrapException(message);
        }
        return value;
    }

    public T unwrap() {
        if (!isOk) {
            throw new UnwrapException("Status was not ok");
        }
        return value;
    }

    public Error expectErr(String message) {
        if (isOk) {
            throw new UnwrapException(message);
        }
        return error;
    }

    public Error unwrapErr() {
        if (isOk) {
            throw new UnwrapException("Status was ok");
        }
        return error;
    }

    public T unwrapOr(T defaultValue) {
        return isOk ? value : defaultValue;
    }

    public List<Warning> getWarnings() {
        return List.copyOf(warnings);
    }

    public void setValue(T value) {
        if (isFinal) {
            return;
        }

        isOk = true;
        this.value = value;
    }

    public void setError(Error error) {
        if (isFinal) {
            return;
        }

        isOk = false;
        this.error = error;
    }

    public void addWarning(Warning warning) {
        if (isFinal) {
            return;
        }

        warnings.add(warning);
    }

    public void addWarnings(Collection<Warning> warnings) {
        if (isFinal) {
            return;
        }

        this.warnings.addAll(warnings);
    }
}
