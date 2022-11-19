package io.github.liquip.paper.standalone.item.crafting;

import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface CraftingRecipe {
    boolean matches(@NotNull ItemStack @NotNull [] grid, @NotNull NamespacedKey @NotNull [] idGrid);

    @NotNull ItemStack getResult();

    void craft(@NotNull ItemStack @NotNull [] grid, @NotNull InventoryClickEvent event);
}
