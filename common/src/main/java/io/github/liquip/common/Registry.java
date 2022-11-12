package io.github.liquip.common;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;

public class Registry<T extends Keyed> implements io.github.liquip.api.Registry<T> {
    private final HashMap<NamespacedKey, T> map;

    public Registry() {
        this(16);
    }

    public Registry(int initialCapacity) {
        this.map = new HashMap<>(initialCapacity);
    }

    @Override
    public @Nullable T get(@NotNull NamespacedKey key) {
        return map.get(key);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return map.values().iterator();
    }

    @Override
    public void register(@NonNull NamespacedKey key, @NonNull T value) {
        map.put(key, value);
    }
}
