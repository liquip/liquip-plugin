package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.crafting.CraftMatrix;
import io.github.liquip.api.item.crafting.ShapedRecipe;
import net.kyori.adventure.key.KeyedValue;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class ShapedRecipeImpl implements ShapedRecipe {
    private final Item item;
    private final CraftMatrix matrix;

    public ShapedRecipeImpl(@NonNull Item item, @NonNull List<KeyedValue<Integer>> shape) {
        this.item = item;
        this.matrix = new ShapedCraftMatrixImpl(shape);
    }

    @Override
    public @NonNull CraftMatrix getMatrix() {
        return this.matrix;
    }

    @Override
    public void apply(@NonNull ItemStack @NonNull [] stacks) {

    }

    @Override
    public @NonNull ItemStack getResult(@NonNull List<KeyedValue<Integer>> stacks) {
        return this.item.newItemStack();
    }
}
