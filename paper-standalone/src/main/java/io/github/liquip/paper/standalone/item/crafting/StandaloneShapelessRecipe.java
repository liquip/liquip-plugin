package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.crafting.CraftMatrix;
import io.github.liquip.api.item.crafting.ShapelessRecipe;
import net.kyori.adventure.key.KeyedValue;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class StandaloneShapelessRecipe implements ShapelessRecipe {
    private final Item item;
    private final CraftMatrix matrix;

    public StandaloneShapelessRecipe(@NotNull Item item, @NotNull Set<KeyedValue<Integer>> ingredients) {
        Objects.requireNonNull(item);
        Objects.requireNonNull(ingredients);
        this.item = item;
        this.matrix = new ShapelessCraftMatrix(ingredients);
    }

    @Override
    public @NotNull CraftMatrix getMatrix() {
        return this.matrix;
    }

    @Override
    public void apply(@NotNull ItemStack @NotNull [] stacks) {
        Objects.requireNonNull(stacks);
        // TODO - implement
    }

    @Override
    public @NotNull ItemStack getResult(@NotNull List<KeyedValue<Integer>> stacks) {
        Objects.requireNonNull(stacks);
        return this.item.newItemStack();
    }

    @Override
    public @NotNull ItemStack getShowcaseItem() {
        return this.item.newItemStack();
    }
}
