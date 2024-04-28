package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.item.crafting.CraftMatrix;
import net.kyori.adventure.key.KeyedValue;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UnboundCraftMatrix implements CraftMatrix {
    private final List<KeyedValue<Integer>> stacks;
    private boolean shaped;

    public UnboundCraftMatrix(boolean shaped, @NotNull List<KeyedValue<Integer>> stacks) {
        Objects.requireNonNull(stacks);
        this.shaped = shaped;
        this.stacks = Collections.unmodifiableList(stacks);
    }

    @Override
    public boolean isRecipeBound() {
        return false;
    }

    @Override
    public boolean isShaped() {
        return this.shaped;
    }

    public void setShaped(boolean shaped) {
        this.shaped = shaped;
    }

    @Override
    public boolean matches(@NotNull CraftMatrix that) {
        Objects.requireNonNull(that);
        return that.matches(this);
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (KeyedValue<Integer> element : this.stacks) {
            result = 31 * result + (element == null ? 0 : element.key().hashCode());
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CraftMatrix that)) {
            return false;
        }
        return that.matches(this);
    }

    @Override
    public @NotNull List<KeyedValue<Integer>> getStacks() throws IllegalStateException {
        return this.stacks;
    }
}
