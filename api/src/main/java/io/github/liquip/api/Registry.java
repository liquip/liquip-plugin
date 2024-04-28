package io.github.liquip.api;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A registry holding values of type {@link V} accessible by a {@link Key}.
 *
 * @param <V> the type of value being stored
 * @since 0.0.1-alpha
 */
public interface Registry<V extends Keyed> extends Iterable<V> {
    /**
     * Registers the specified {@link V} under the specified {@link Key}.
     *
     * @param key   the key the value will be stored under
     * @param value the value that will be stored
     * @since 0.0.1-alpha
     */
    void register(@NotNull Key key, @NotNull V value);

    /**
     * Unregisters the specified {@link V} under the specified {@link Key}.
     *
     * @param key the key of the value to be unregistered
     * @since 2.0.0
     */
    void unregister(@NotNull Key key);

    /**
     * Retrieves the {@link V} stored under the specified {@link Key}.
     *
     * @param key the key the value is stored under
     * @return the value that is stored under the specified key
     * @since 0.0.1-alpha
     */
    @Nullable
    V get(@NotNull Key key);
}
