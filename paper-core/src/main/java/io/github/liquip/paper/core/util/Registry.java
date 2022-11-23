package io.github.liquip.paper.core.util;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;

public class Registry<T extends Keyed> implements io.github.liquip.api.Registry<T> {
    private final HashMap<Key, T> map;

    public Registry() {
        this(16);
    }

    public Registry(int initialCapacity) {
        this.map = new HashMap<>(initialCapacity);
    }

    @Override
    public @Nullable T get(@NotNull Key key) {
        return map.get(key);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return map.values().iterator();
    }

    @Override
    public void register(@NonNull Key key, @NonNull T value) {
        map.put(key, value);
    }
}
