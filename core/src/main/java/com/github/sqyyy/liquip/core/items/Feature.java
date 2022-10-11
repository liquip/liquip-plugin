package com.github.sqyyy.liquip.core.items;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Feature {
    default void initialize(@NotNull LiquipItem item) {
    }

    default void apply(@NotNull ItemStack itemStack) {
    }
}
