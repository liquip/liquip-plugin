package io.github.liquip.api.item.crafting;

import net.kyori.adventure.key.KeyedValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CraftMatrix {
    boolean isRecipeBound();

    boolean isShaped();

    boolean matches(@NotNull CraftMatrix that);

    @NotNull List<KeyedValue<Integer>> getStacks() throws IllegalStateException;

    int hashCode();

    boolean equals(Object that);
}
