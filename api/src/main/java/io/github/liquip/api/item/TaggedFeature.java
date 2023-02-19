package io.github.liquip.api.item;

import io.github.liquip.api.config.ConfigElement;
import org.bukkit.Keyed;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TaggedFeature<T> extends Keyed {
    default @Nullable T initialize(@NotNull Item item, @NotNull ConfigElement element) {
        return null;
    }

    default void apply(@NotNull Item item, @NotNull ItemStack itemStack, @NotNull T object) {
    }
}
