package io.github.liquip.api.item.crafting;

import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Recipe {
    @NonNull CraftMatrix getMatrix();

    boolean matches(@NonNull ItemStack @NonNull [] craftingMatrix);
}
