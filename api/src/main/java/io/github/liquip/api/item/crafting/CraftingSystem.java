package io.github.liquip.api.item.crafting;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public interface CraftingSystem {
    void registerShapedRecipe(@NotNull ShapedRecipe recipe);

    void unregisterShapedRecipe(@NotNull ShapedRecipe recipe);

    void registerShapelessRecipe(@NotNull ShapelessRecipe recipe);

    void unregisterShapelessRecipe(@NotNull ShapelessRecipe recipe);

    @Nullable
    ShapedRecipe getShapedRecipe(@NotNull CraftMatrix craftMatrix);

    @Nullable
    ShapelessRecipe getShapelessRecipe(@NotNull CraftMatrix craftMatrix);

    @NotNull
    Iterator<ShapedRecipe> shapedIterator();

    @NotNull
    Iterator<ShapelessRecipe> shapelessIterator();
}
