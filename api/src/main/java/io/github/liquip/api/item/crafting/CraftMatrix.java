package io.github.liquip.api.item.crafting;

import net.kyori.adventure.key.KeyedValue;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public interface CraftMatrix {
    boolean isRecipeBound();

    boolean isShaped();

    boolean matches(@NonNull CraftMatrix that);

    @NonNull List<KeyedValue<Integer>> getStacks() throws IllegalStateException;

    int hashCode();

    boolean equals(Object that);
}
