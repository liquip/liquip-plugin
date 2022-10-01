package com.github.sqyyy.liquip.items;

import org.bukkit.inventory.ItemStack;

public interface Feature {
    // TODO set feature-format
    void initialize(LiquipItem item);

    void apply(ItemStack itemStack);
}
