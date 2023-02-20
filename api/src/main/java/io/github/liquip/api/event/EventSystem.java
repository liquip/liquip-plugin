package io.github.liquip.api.event;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The event system interface to interact with Liquip-specific events.
 */
public interface EventSystem {
    void registerBus(@NotNull NamespacedKey key, @NotNull EventBus<?, ?> bus);

    @Nullable EventBus<?, ?> getBus(@NotNull NamespacedKey key);
}
