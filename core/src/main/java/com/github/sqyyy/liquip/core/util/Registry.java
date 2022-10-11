package com.github.sqyyy.liquip.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface Registry<T> {
    boolean register(@NotNull Identifier identifier, @NotNull T value);

    boolean unregister(@NotNull Identifier identifier);

    boolean isRegistered(@NotNull Identifier identifier);

    void lock();

    boolean isLocked();

    @Nullable T get(@NotNull Identifier identifier);

    @NotNull Set<@NotNull Identifier> keySet();
}
