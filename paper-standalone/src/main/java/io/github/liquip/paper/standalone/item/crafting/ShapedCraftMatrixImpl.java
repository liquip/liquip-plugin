package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.item.crafting.CraftMatrix;
import io.github.liquip.api.item.crafting.ShapedCraftMatrix;
import net.kyori.adventure.key.KeyedValue;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class ShapedCraftMatrixImpl implements ShapedCraftMatrix {
    private final KeyedValue<Integer>[] ingredients;
    private final int hashCode;

    public ShapedCraftMatrixImpl(@Nullable KeyedValue<Integer> @NonNull [] ingredients) {
        this.ingredients = ingredients.clone();
        this.hashCode = Arrays.hashCode(
            Arrays.stream(ingredients).filter(Objects::nonNull).map(KeyedValue::key).toArray());
    }

    @Nullable
    @Override
    public KeyedValue<Integer> @NonNull [] getIngredients() {
        return this.ingredients.clone();
    }

    public boolean matches(@NonNull CraftMatrix that) {
        if (!(that instanceof ShapedCraftMatrix other)) {
            return false;
        }
        final KeyedValue<Integer>[] otherIngredients = other.getIngredients();
        for (int i = 0; i < 9; i++) {
            final KeyedValue<Integer> ingredient = this.ingredients[i];
            final KeyedValue<Integer> otherIngredient = otherIngredients[i];
            if (!ingredient.key().equals(otherIngredient.key()) ||
                ingredient.value() < otherIngredient.value()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CraftMatrix that))
            return false;
        return this.matches(that);
    }
}
