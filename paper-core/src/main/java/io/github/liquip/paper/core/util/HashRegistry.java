package io.github.liquip.paper.core.util;

import io.github.liquip.api.Registry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

/**
 * Implementation of the {@link Registry} interface with a {@link HashMap}.
 *
 * @param <V> the type of the value being stored
 */
public class HashRegistry<V extends Keyed> implements Registry<V> {
    private final HashMap<Key, V> map;

    public HashRegistry() {
        this(16);
    }

    public HashRegistry(int initialCapacity) {
        this.map = new HashMap<>(initialCapacity);
    }

    @Override
    public @NotNull Iterator<V> iterator() {
        return this.map.values()
            .iterator();
    }

    @Override
    public void register(@NotNull Key key, @NotNull V value) {
        Objects.requireNonNull(value);
        this.map.put(key, value);
    }

    @Override
    public void unregister(@NotNull Key key) {
        Objects.requireNonNull(key);
        this.map.remove(key);
    }

    @Override
    public @Nullable V get(@NotNull Key key) {
        Objects.requireNonNull(key);
        return this.map.get(key);
    }
}
