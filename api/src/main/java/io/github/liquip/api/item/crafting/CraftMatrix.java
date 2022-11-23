package io.github.liquip.api.item.crafting;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface CraftMatrix {
    boolean isShaped();

    boolean matches(@NonNull CraftMatrix that);

    int hashCode();

    boolean equals(Object that);
}
