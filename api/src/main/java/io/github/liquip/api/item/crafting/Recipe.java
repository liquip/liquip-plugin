package io.github.liquip.api.item.crafting;

import net.kyori.adventure.key.KeyedValue;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Recipe {
    @NotNull CraftMatrix getMatrix();

    void apply(@NotNull ItemStack @NotNull [] stacks);

    @NotNull ItemStack getResult(@NotNull List<KeyedValue<Integer>> stacks);

    @NotNull ItemStack getShowcaseItem();
}
