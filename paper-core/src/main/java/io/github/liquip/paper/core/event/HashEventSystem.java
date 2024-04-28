package io.github.liquip.paper.core.event;

import io.github.liquip.api.event.EventBus;
import io.github.liquip.api.event.EventSystem;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HashEventSystem implements EventSystem {
    protected final Map<NamespacedKey, EventBus<?, ?>> buses;

    public HashEventSystem() {
        this(16);
    }

    public HashEventSystem(int initialCapacity) {
        this.buses = new HashMap<>(initialCapacity);
    }

    @Override
    public void registerBus(@NotNull NamespacedKey key, @NotNull EventBus<?, ?> bus) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(bus);
        this.buses.put(key, bus);
    }

    @Override
    public @Nullable EventBus<?, ?> getBus(@NotNull NamespacedKey key) {
        Objects.requireNonNull(key);
        return this.buses.get(key);
    }
}
