package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.item.crafting.CraftMatrix;
import net.kyori.adventure.key.KeyedValue;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@ApiStatus.Experimental
public class ShapelessCraftMatrixImpl implements CraftMatrix {
    private final Set<KeyedValue<Integer>> ingredients;
    private final int hashCode;

    public ShapelessCraftMatrixImpl(@NonNull Set<KeyedValue<Integer>> ingredients) {
        this.ingredients = ingredients;
        this.hashCode = ingredients.hashCode();
    }

    public @NonNull Set<KeyedValue<Integer>> getIngredients() {
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
    public boolean matches(@NonNull CraftMatrix that) {
        // TODO - implement
        return false;
    }

    @Override
    public @NonNull List<KeyedValue<Integer>> getStacks() throws IllegalStateException {
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
