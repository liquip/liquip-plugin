package io.github.liquip.api.item.crafting;

import net.kyori.adventure.key.KeyedValue;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;

public interface ShapelessCraftMatrix extends CraftMatrix {
    @NonNull Set<KeyedValue<Integer>> getIngredients();

    @Override
    default boolean isShaped() {
        return false;
    }
}
