package io.github.liquip.paper.core.util;

import io.github.liquip.api.Registry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class HashRegistry<T extends Keyed> implements Registry<T> {
    private final HashMap<Key, T> map;

    public HashRegistry() {
        this(16);
    }

    public HashRegistry(int initialCapacity) {
        this.map = new HashMap<>(initialCapacity);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return this.map.values()
            .iterator();
    }

    @Override
    public void register(@NotNull Key key, @NotNull T value) {
        Objects.requireNonNull(value);
        this.map.put(key, value);
    }

    @Override
    public void unregister(@NotNull Key key) {
        Objects.requireNonNull(key);
        this.map.remove(key);
    }

    @Override
    public @Nullable T get(@NotNull Key key) {
        Objects.requireNonNull(key);
        return this.map.get(key);
    }
}
