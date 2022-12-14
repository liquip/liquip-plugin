package io.github.liquip.api.item.crafting;

import net.kyori.adventure.key.KeyedValue;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public interface Recipe {
    @NonNull CraftMatrix getMatrix();

    boolean matches(@NonNull ItemStack @NonNull [] craftingMatrix);

    void apply(@NonNull ItemStack @NonNull [] stacks);

    @NonNull ItemStack getResult(List<KeyedValue<Integer>> stacks);
}
