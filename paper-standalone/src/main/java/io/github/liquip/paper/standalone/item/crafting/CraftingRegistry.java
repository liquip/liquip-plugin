package io.github.liquip.paper.standalone.item.crafting;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CraftingRegistry {
    private final Map<CraftingHashObject, CraftingRecipe> recipes;

    public CraftingRegistry() {
        recipes = new HashMap<>();
    }

    public boolean register(@NotNull CraftingHashObject craftingHashObject, @NotNull CraftingRecipe recipe) {
        if (recipes.containsKey(craftingHashObject)) {
            return false;
        }
        recipes.put(craftingHashObject, recipe);
        return true;
    }

    public boolean isRegistered(@NotNull CraftingHashObject craftingHashObject) {
        return recipes.containsKey(craftingHashObject);
    }

    public CraftingRecipe get(@NotNull CraftingHashObject craftingHashObject) {
        return recipes.get(craftingHashObject);
    }
}
