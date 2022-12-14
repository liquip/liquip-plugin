package io.github.liquip.api.item.crafting;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface CraftingSystem {
    void registerShapedRecipe(@NonNull ShapedRecipe recipe);

    void registerShapelessRecipe(@NonNull ShapelessRecipe recipe);

    @Nullable Recipe getShapedRecipe(@NonNull CraftMatrix craftMatrix);

    @Nullable Recipe getShapelessRecipe(@NonNull CraftMatrix craftMatrix);
}
