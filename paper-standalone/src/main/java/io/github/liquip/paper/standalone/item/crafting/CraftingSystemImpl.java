package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.item.crafting.CraftMatrix;
import io.github.liquip.api.item.crafting.CraftingSystem;
import io.github.liquip.api.item.crafting.ShapedRecipe;
import io.github.liquip.api.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

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
    public void registerShapedRecipe(@NotNull ShapedRecipe recipe) {
        Objects.requireNonNull(recipe);
        this.shapedMap.put(recipe.getMatrix(), recipe);
    }

    @Override
    public void unregisterShapedRecipe(@NotNull ShapedRecipe recipe) {
        Objects.requireNonNull(recipe);
        for (final Map.Entry<CraftMatrix, ShapedRecipe> entry : this.shapedMap.entrySet()) {
            if (entry.getValue() == recipe) {
                this.shapedMap.remove(entry.getKey(), recipe);
                break;
            }
        }
    }

    @Override
    public void registerShapelessRecipe(@NotNull ShapelessRecipe recipe) {
        Objects.requireNonNull(recipe);
        this.shapelessMap.put(recipe.getMatrix(), recipe);
    }

    @Override
    public void unregisterShapelessRecipe(@NotNull ShapelessRecipe recipe) {
        Objects.requireNonNull(recipe);
        for (final Map.Entry<CraftMatrix, ShapelessRecipe> entry : this.shapelessMap.entrySet()) {
            if (entry.getValue() == recipe) {
                this.shapelessMap.remove(entry.getKey(), recipe);
                break;
            }
        }
    }

    @Override
    public @Nullable ShapedRecipe getShapedRecipe(@NotNull CraftMatrix craftMatrix) {
        Objects.requireNonNull(craftMatrix);
        if (!craftMatrix.isShaped()) {
            return null;
        }
        return this.shapedMap.get(craftMatrix);
    }

    @Override
    public @Nullable ShapelessRecipe getShapelessRecipe(@NotNull CraftMatrix craftMatrix) {
        Objects.requireNonNull(craftMatrix);
        if (craftMatrix.isShaped()) {
            return null;
        }
        return this.shapelessMap.get(craftMatrix);
    }

    @Override
    public @NotNull Iterator<ShapedRecipe> shapedIterator() {
        return this.shapedMap.values()
            .iterator();
    }

    @Override
    public @NotNull Iterator<ShapelessRecipe> shapelessIterator() {
        return this.shapelessMap.values()
            .iterator();
    }
}
