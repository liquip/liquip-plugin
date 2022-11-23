package io.github.liquip.api.item.crafting;

import net.kyori.adventure.key.KeyedValue;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ShapedCraftMatrix extends CraftMatrix {
    @Nullable KeyedValue<Integer> @NonNull [] getIngredients();

    @Override
    default boolean isShaped() {
        return true;
    }
}
