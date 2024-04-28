package io.github.liquip.api.config;

import org.jetbrains.annotations.NotNull;

public interface ConfigObject {
    boolean hasElement(@NotNull String key);

    boolean isBoolean(@NotNull String key);

    boolean isInt(@NotNull String key);

    boolean isDouble(@NotNull String key);

    boolean isString(@NotNull String key);

    boolean isArray(@NotNull String key);

    boolean isObject(@NotNull String key);

    boolean getBoolean(@NotNull String key);

    int getInt(@NotNull String key);

    double getDouble(@NotNull String key);

    @NotNull
    String getString(@NotNull String key);

    @NotNull
    ConfigArray getArray(@NotNull String key);

    @NotNull
    ConfigObject getObject(@NotNull String key);
}
