package com.github.sqyyy.liquip.core.system.craft;

import com.github.sqyyy.liquip.core.util.Identifier;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface CraftingRecipe {
    boolean matches(@NotNull ItemStack @NotNull [] grid, @NotNull Identifier @NotNull [] idGrid);

    @NotNull ItemStack getResult();

    void craft(@NotNull ItemStack @NotNull [] grid, @NotNull InventoryClickEvent event);
}
