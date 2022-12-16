package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.crafting.CraftMatrix;
import io.github.liquip.api.item.crafting.ShapelessRecipe;
import net.kyori.adventure.key.KeyedValue;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Set;

public class ShapelessRecipeImpl implements ShapelessRecipe {
    private final Item item;
    private final CraftMatrix matrix;

    public ShapelessRecipeImpl(@NonNull Item item, @NonNull Set<KeyedValue<Integer>> ingredients) {
        this.item = item;
        this.matrix = new ShapelessCraftMatrixImpl(ingredients);
    }

    @Override
    public @NonNull CraftMatrix getMatrix() {
        return this.matrix;
    }

    @Override
    public void apply(@NonNull ItemStack @NonNull [] stacks) {
        // TODO - implement
    }

    @Override
    public @NonNull ItemStack getResult(@NonNull List<KeyedValue<Integer>> stacks) {
        return this.item.newItemStack();
    }
}
