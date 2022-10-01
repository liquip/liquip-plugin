package com.github.sqyyy.liquip.util;

import java.util.HashMap;
import java.util.Map;

public class BasicRegistry<T> implements Registry<T> {
    private final Map<Identifier, T> registry;
    private boolean locked;

    public BasicRegistry() {
        registry = new HashMap<>();
        locked = false;
    }

    public boolean register(Identifier identifier, T value) {
        if (locked) {
            return false;
        }
        if (registry.containsKey(identifier)) {
            return false;
        }
        registry.put(identifier, value);
        return true;
    }

    public boolean unregister(Identifier identifier) {
        if (locked) {
            return false;
        }
        if (!registry.containsKey(identifier)) {
            return false;
        }
        registry.remove(identifier);
        return true;
    }

    public boolean isRegistered(Identifier identifier) {
        return registry.containsKey(identifier);
    }

    public void lock() {
        locked = true;
    }

    public boolean isLocked() {
        return locked;
    }

    public T get(Identifier identifier) {
        return registry.get(identifier);
    }
}
