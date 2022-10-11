package com.github.sqyyy.liquip.core.util.impl;

import com.github.sqyyy.liquip.core.util.Identifier;
import com.github.sqyyy.liquip.core.util.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BasicRegistry<T> implements Registry<@NotNull T> {
    private final Map<Identifier, T> registry;
    private boolean locked;

    public BasicRegistry() {
        registry = new HashMap<>();
        locked = false;
    }

    public boolean register(@NotNull Identifier identifier, @NotNull T value) {
        if (locked) {
            return false;
        }
        if (registry.containsKey(identifier)) {
            return false;
        }
        registry.put(identifier, value);
        return true;
    }

    public boolean unregister(@NotNull Identifier identifier) {
        if (locked) {
            return false;
        }
        if (!registry.containsKey(identifier)) {
            return false;
        }
        registry.remove(identifier);
        return true;
    }

    public boolean isRegistered(@NotNull Identifier identifier) {
        return registry.containsKey(identifier);
    }

    public void lock() {
        locked = true;
    }

    public boolean isLocked() {
        return locked;
    }

    public @Nullable T get(@NotNull Identifier identifier) {
        return registry.get(identifier);
    }

    @Override
    public @NotNull Set<@NotNull Identifier> keySet() {
        return registry.keySet();
    }
}
