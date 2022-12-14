package io.github.liquip.api.item.crafting;

import net.kyori.adventure.key.KeyedValue;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Recipe {
    @NonNull CraftMatrix getMatrix();

    void apply(@NonNull ItemStack @NonNull [] stacks);

    @NonNull ItemStack getResult(@NonNull List<KeyedValue<Integer>> stacks);

    @NotNull ItemStack getShowcaseItem();
}
