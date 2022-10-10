package com.github.sqyyy.liquip.core.system.craft;

import com.github.sqyyy.liquip.core.Liquip;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CraftingRegistry {
    private final Map<CraftingHashObject, CraftingRecipe> recipes;

    public CraftingRegistry() {
        recipes = new HashMap<>();
    }

    public boolean register(@NotNull CraftingHashObject craftingHashObject, @NotNull CraftingRecipe recipe) {
        Bukkit.broadcast(Component.text("registered new recipe in Registry " + craftingHashObject.hashCode()));
        Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger().debug("Recipe registered: {}", craftingHashObject);
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
