package io.github.liquip.api.item.crafting;

import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface CraftMatrix {
    @Nullable NamespacedKey @NonNull [] getKeys();

    int @NonNull [] getAmounts();

    boolean matches(@NonNull CraftMatrix that);

    int hashCode();

    boolean equals(Object that);
}
