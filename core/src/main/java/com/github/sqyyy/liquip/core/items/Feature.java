package com.github.sqyyy.liquip.core.items;

import org.bukkit.inventory.ItemStack;

public interface Feature {
    default void initialize(LiquipItem item) {
    }

    default void apply(ItemStack itemStack) {
    }
}
