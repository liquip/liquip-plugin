package com.github.sqyyy.liquip.core.items;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface LiquipEnchantment {
    void apply(@NotNull ItemStack itemStack, int level);
}
