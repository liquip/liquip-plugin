package io.github.liquip.api;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A registry holding values accessible by a {@link Key}.
 *
 * @param <T> the type of value being stored
 * @since 0.0.1-alpha
 */
public interface Registry<T extends Keyed> extends Iterable<T> {
    /**
     * Registers the specified {@link T} under the specified {@link Key}.
     *
     * @param key   the {@link Key} the {@link T} will be stored under
     * @param value the {@link T} that will be stored
     * @since 0.0.1-alpha
     */
    void register(@NonNull Key key, @NonNull T value);

    /**
     * Retrieves the {@link T} stored under the specified {@link Key}.
     *
     * @param key the {@link Key} the {@link T} is stored under
     * @return the {@link T} that is stored under the {@link Key}
     * @since 0.0.1-alpha
     */
    @Nullable T get(@NonNull Key key);
}
