package io.github.liquip.api;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface Registry<T extends Keyed> extends Iterable<T> {
    void register(@NonNull Key key, @NonNull T value);

    void unregister(@NonNull Key key);

    @Nullable T get(@NonNull Key key);
}
