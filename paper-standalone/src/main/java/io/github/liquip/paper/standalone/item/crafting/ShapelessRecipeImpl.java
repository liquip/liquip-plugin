package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.crafting.CraftMatrix;
import io.github.liquip.api.item.crafting.ShapelessRecipe;
import net.kyori.adventure.key.KeyedValue;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ShapelessRecipeImpl implements ShapelessRecipe {
    private final Item item;
    private final CraftMatrix matrix;

    public ShapelessRecipeImpl(@NotNull Item item, @NotNull Set<KeyedValue<Integer>> ingredients) {
        this.item = item;
        this.matrix = new ShapelessCraftMatrixImpl(ingredients);
    }

    @Override
    public @NotNull CraftMatrix getMatrix() {
        return this.matrix;
    }

    @Override
    public void apply(@NotNull ItemStack @NotNull [] stacks) {
        // TODO - implement
    }

    @Override
    public @NotNull ItemStack getResult(@NotNull List<KeyedValue<Integer>> stacks) {
        return this.item.newItemStack();
    }

    @Override
    public @NotNull ItemStack getShowcaseItem() {
        return this.item.newItemStack();
    }
}
