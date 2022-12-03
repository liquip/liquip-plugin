package io.github.liquip.api.config;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface ConfigArray {
    int size();

    boolean isBoolean(int index);

    boolean isInt(int index);

    boolean isDouble(int index);

    boolean isString(int index);

    boolean isArray(int index);

    boolean isObject(int index);

    boolean getBoolean(int index);

    int getInt(int index);

    double getDouble(int index);

    @NonNull String getString(int index);

    @NonNull ConfigArray getArray(int index);

    @NonNull ConfigObject getObject(int index);
}
