package io.github.liquip.api.config;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Experimental
public interface ConfigTable {
    boolean hasElement(@NonNull String key);

    boolean isBoolean(@NonNull String key);

    boolean isInt(@NonNull String key);

    boolean isFloat(@NonNull String key);

    boolean isDouble(@NonNull String key);

    boolean isString(@NonNull String key);

    boolean isList(@NonNull String key);

    boolean isTable(@NonNull String key);

    boolean getBoolean(@NonNull String key);

    int getInt(@NonNull String key);

    float getFloat(@NonNull String key);

    float getDouble(@NonNull String key);

    @NonNull String getString(@NonNull String key);

    @NonNull List<Object> getList(@NonNull String key);

    @NonNull ConfigTable getTable(@NonNull String key);
}
