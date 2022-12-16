package io.github.liquip.api.item;

import org.bukkit.Keyed;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Enchantment extends Keyed {
    default void initialize(@NonNull Item item, int level) {
    }

    default void apply(@NonNull Item item, @NonNull ItemStack itemStack, int level) {
    }
}
