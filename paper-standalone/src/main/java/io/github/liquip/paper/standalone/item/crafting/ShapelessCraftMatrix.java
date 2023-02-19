package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.item.crafting.CraftMatrix;
import net.kyori.adventure.key.KeyedValue;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@ApiStatus.Experimental
public class ShapelessCraftMatrix implements CraftMatrix {
    private final Set<KeyedValue<Integer>> ingredients;
    private final int hashCode;

    public ShapelessCraftMatrix(@NotNull Set<KeyedValue<Integer>> ingredients) {
        Objects.requireNonNull(ingredients);
        this.ingredients = ingredients;
        this.hashCode = ingredients.hashCode();
    }

    public @NotNull Set<KeyedValue<Integer>> getIngredients() {
        return Collections.unmodifiableSet(this.ingredients);
    }

    @Override
    public boolean isRecipeBound() {
        return true;
    }

    @Override
    public boolean isShaped() {
        return false;
    }

    @Override
    public boolean matches(@NotNull CraftMatrix that) {
        Objects.requireNonNull(that);
        // TODO - implement
        return false;
    }

    @Override
    public @NotNull List<KeyedValue<Integer>> getStacks() throws IllegalStateException {
        throw new IllegalStateException();
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CraftMatrix that)) {
            return false;
        }
        return this.matches(that);
    }
}
