package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.item.crafting.CraftMatrix;
import net.kyori.adventure.key.KeyedValue;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.List;

public class ShapedCraftMatrixImpl implements CraftMatrix {
    private final List<KeyedValue<Integer>> shape;
    private final int hashCode;

    public ShapedCraftMatrixImpl(@NonNull List<KeyedValue<Integer>> shape) {
        this.shape = Collections.unmodifiableList(shape);
        int result = 1;
        for (KeyedValue<Integer> element : this.shape) {
            result = 31 * result + (element == null ? 0 : element.key()
                .hashCode());
        }
        this.hashCode = result;
    }

    @Override
    public boolean isRecipeBound() {
        return true;
    }

    @Override
    public boolean isShaped() {
        return true;
    }

    @Override
    public boolean matches(@NonNull CraftMatrix that) {
        if (that.isRecipeBound() || !that.isShaped()) {
            return false;
        }
        final List<KeyedValue<Integer>> otherIngredients = that.getStacks();
        for (int i = 0; i < 9; i++) {
            final KeyedValue<Integer> ingredient = this.shape.get(i);
            final KeyedValue<Integer> otherIngredient = otherIngredients.get(i);
            if (ingredient == null) {
                if (otherIngredient != null) {
                    return false;
                }
                continue;
            }
            if (otherIngredient == null) {
                return false;
            }
            if (!ingredient.key()
                .equals(otherIngredient.key()) || ingredient.value() > otherIngredient.value()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NonNull List<KeyedValue<Integer>> getStacks() {
        return this.shape;
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
