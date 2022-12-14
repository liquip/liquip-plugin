package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.item.crafting.CraftMatrix;
import net.kyori.adventure.key.KeyedValue;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.List;

public class UnboundCraftMatrixImpl implements CraftMatrix {
    private final List<KeyedValue<Integer>> stacks;
    private boolean shaped;

    public UnboundCraftMatrixImpl(boolean shaped, @NonNull List<KeyedValue<Integer>> stacks) {
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
    public boolean matches(@NonNull CraftMatrix that) {
        return that.matches(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CraftMatrix that)) {
            return false;
        }
        return that.matches(this);
    }

    @Override
    public @NonNull List<KeyedValue<Integer>> getStacks() throws IllegalStateException {
        return this.stacks;
    }
}
