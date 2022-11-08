package io.github.liquip.api.item;

import io.github.liquip.api.config.ConfigTable;
import org.bukkit.Keyed;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface TaggedFeature<T> extends Keyed {
    default @Nullable T initialize(@NonNull Item item, @NonNull ConfigTable configTable) {
        return null;
    }

    default void apply(@NonNull Item item, @NonNull ItemStack itemStack, @Nullable T object) {
    }
}
