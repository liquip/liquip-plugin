package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.item.crafting.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CraftingSystemImpl implements CraftingSystem {
    private final Map<CraftMatrix, ShapedRecipe> shapedMap;
    private final Map<CraftMatrix, ShapelessRecipe> shapelessMap;

    public CraftingSystemImpl() {
        this(16);
    }

    public CraftingSystemImpl(int initialCapacity) {
        this.shapedMap = new HashMap<>(initialCapacity);
        this.shapelessMap = new HashMap<>(initialCapacity);
    }

    @Override
    public void registerShapedRecipe(@NonNull ShapedRecipe recipe) {
        this.shapedMap.put(recipe.getMatrix(), recipe);
    }

    @Override
    public void registerShapelessRecipe(@NonNull ShapelessRecipe recipe) {
        this.shapelessMap.put(recipe.getMatrix(), recipe);
    }

    @Override
    public @Nullable Recipe getShapedRecipe(@NonNull CraftMatrix craftMatrix) {
        if (!craftMatrix.isShaped()) {
            return null;
        }
        return this.shapedMap.get(craftMatrix);
    }

    @Override
    public @Nullable Recipe getShapelessRecipe(@NonNull CraftMatrix craftMatrix) {
        if (craftMatrix.isShaped()) {
            return null;
        }
        return this.shapelessMap.get(craftMatrix);
    }
}
