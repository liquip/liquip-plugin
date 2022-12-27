package io.github.liquip.api;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A registry holding values of type {@link T} accessible by a {@link Key}.
 *
 * @param <T> the type of value being stored
 * @since 0.0.1-alpha
 */
public interface Registry<T extends Keyed> extends Iterable<T> {
    /**
     * Registers the specified {@link T} under the specified {@link Key}.
     *
     * @param key   the key the value will be stored under
     * @param value the value that will be stored
     * @since 0.0.1-alpha
     */
    void register(@NonNull Key key, @NonNull T value);

    void unregister(@NonNull Key key);

    /**
     * Retrieves the {@link T} stored under the specified {@link Key}.
     *
     * @param key the key the value is stored under
     * @return the value that is stored under the specified key
     * @since 0.0.1-alpha
     */
    @Nullable T get(@NonNull Key key);
}
