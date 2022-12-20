package io.github.liquip.paper.core.util;

import io.github.liquip.api.Registry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;

public class RegistryImpl<T extends Keyed> implements Registry<T> {
    private final HashMap<Key, T> map;

    public RegistryImpl() {
        this(16);
    }

    public RegistryImpl(int initialCapacity) {
        this.map = new HashMap<>(initialCapacity);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return this.map.values().iterator();
    }

    @Override
    public void register(@NonNull Key key, @NonNull T value) {
        this.map.put(key, value);
    }

    @Override
    public void unregister(@NonNull Key key) {
        this.map.remove(key);
    }

    @Override
    public @Nullable T get(@NotNull Key key) {
        return this.map.get(key);
    }
}
