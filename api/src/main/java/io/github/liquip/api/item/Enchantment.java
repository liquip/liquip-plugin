package io.github.liquip.api.item;

import org.bukkit.Keyed;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Enchantment extends Keyed {
    default void initialize(@NotNull Item item, int level) {
    }

    default void apply(@NotNull Item item, @NotNull ItemStack itemStack, int level) {
    }
}
