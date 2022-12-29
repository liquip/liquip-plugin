package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.Liquip;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.crafting.Recipe;
import net.kyori.adventure.key.KeyedValue;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CraftingCatalogueEntry {
    private final Recipe recipe;
    private final ItemStack showcaseItem;
    private final ItemStack[] items;
    private final ItemStack result;

    public CraftingCatalogueEntry(@NotNull Recipe recipe, @NotNull ItemStack showcaseItem, @Nullable ItemStack @NotNull [] items,
        @NotNull ItemStack result) {
        this.recipe = recipe;
        this.showcaseItem = showcaseItem;
        this.items = items;
        this.result = result;
    }

    public static @Nullable CraftingCatalogueEntry load(@NotNull Liquip api, @NotNull Recipe recipe) {
        final List<KeyedValue<Integer>> stacks = recipe.getMatrix()
            .getStacks();
        final ItemStack[] items = new ItemStack[9];
        for (int i = 0; i < stacks.size(); i++) {
            final KeyedValue<Integer> stack = stacks.get(i);
            final Material material = Registry.MATERIAL.get((NamespacedKey) stack.key());
            if (material != null) {
                items[i] = new ItemStack(material, stack.value());
                continue;
            }
            final Item item = api.getItemRegistry()
                .get(stack.key());
            if (item == null) {
                return null;
            }
            items[i] = item.newItemStack();
            items[i].setAmount(stack.value());
        }
        return new CraftingCatalogueEntry(recipe, recipe.getShowcaseItem(), items, recipe.getResult(stacks));
    }

    public @NotNull Recipe getRecipe() {
        return this.recipe;
    }

    public @NotNull ItemStack getShowcaseItem() {
        return this.showcaseItem;
    }

    public @Nullable ItemStack @NotNull [] getItems() {
        return this.items;
    }

    public @NotNull ItemStack getResult() {
        return this.result;
    }
}
