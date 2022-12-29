package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.item.crafting.Recipe;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftingCatalogueEntry {
    private final Recipe recipe;
    private final ItemStack icon;
    private final ItemStack[] items;
    private final ItemStack result;

    public CraftingCatalogueEntry(@NotNull Recipe recipe, @NotNull ItemStack icon, @Nullable ItemStack @NotNull [] items,
        @NotNull ItemStack result) {
        this.recipe = recipe;
        this.icon = icon;
        this.items = items;
        this.result = result;
    }

    public @NotNull Recipe getRecipe() {
        return this.recipe;
    }

    public @NotNull ItemStack getIcon() {
        return this.icon;
    }

    public @Nullable ItemStack @NotNull [] getItems() {
        return this.items;
    }

    public @NotNull ItemStack getResult() {
        return this.result;
    }
}
