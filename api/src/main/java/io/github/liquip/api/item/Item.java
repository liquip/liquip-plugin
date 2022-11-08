package io.github.liquip.api.item;

import org.bukkit.Keyed;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Item extends Keyed {
    @NonNull ItemStack newItemStack();
}
