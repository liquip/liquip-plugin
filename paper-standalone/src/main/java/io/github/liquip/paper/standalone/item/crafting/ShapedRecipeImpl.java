package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.crafting.CraftMatrix;
import io.github.liquip.api.item.crafting.ShapedRecipe;
import net.kyori.adventure.key.KeyedValue;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class ShapedRecipeImpl implements ShapedRecipe {
    private final Item item;
    private final int count;
    private final List<KeyedValue<Integer>> shape;
    private final CraftMatrix matrix;

    public ShapedRecipeImpl(@NonNull Item item, int count, @NonNull List<KeyedValue<Integer>> shape) {
        this.item = item;
        this.count = count;
        this.shape = shape;
        this.matrix = new ShapedCraftMatrixImpl(shape);
    }

    @Override
    public @NonNull CraftMatrix getMatrix() {
        return this.matrix;
    }

    @Override
    public void apply(@Nullable ItemStack @NonNull [] stacks) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                final KeyedValue<Integer> ingredient = this.shape.get(row * 3 + column);
                final ItemStack item = stacks[row * 3 + column];
                if (item == null || item.getType() == Material.AIR) {
                    continue;
                }
                item.setAmount(ingredient != null ? item.getAmount() - ingredient.value() : 0);
                if (item.getAmount() < 1) {
                    stacks[row * 3 + column] = null;
                }
            }
        }
    }

    @Override
    public @NonNull ItemStack getResult(@NonNull List<KeyedValue<Integer>> stacks) {
        final ItemStack item = this.item.newItemStack();
        item.setAmount(this.count);
        return item;
    }
}
