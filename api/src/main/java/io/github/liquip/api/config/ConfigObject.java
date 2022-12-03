package io.github.liquip.api.config;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface ConfigObject {
    boolean hasElement(@NonNull String key);

    boolean isBoolean(@NonNull String key);

    boolean isInt(@NonNull String key);

    boolean isDouble(@NonNull String key);

    boolean isString(@NonNull String key);

    boolean isArray(@NonNull String key);

    boolean isObject(@NonNull String key);

    boolean getBoolean(@NonNull String key);

    int getInt(@NonNull String key);

    double getDouble(@NonNull String key);

    @NonNull String getString(@NonNull String key);

    @NonNull ConfigArray getArray(@NonNull String key);

    @NonNull ConfigObject getObject(@NonNull String key);
}
