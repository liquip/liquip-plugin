package io.github.liquip.api.config;

import org.jetbrains.annotations.NotNull;

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

    @NotNull
    String getString(int index);

    @NotNull
    ConfigArray getArray(int index);

    @NotNull
    ConfigObject getObject(int index);
}
