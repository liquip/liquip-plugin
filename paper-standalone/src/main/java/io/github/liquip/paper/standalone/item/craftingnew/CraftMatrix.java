package io.github.liquip.paper.standalone.item.craftingnew;

import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;

public class CraftMatrix {
    private final NamespacedKey[] keys;
    private final int[] amounts;

    public CraftMatrix(@Nullable NamespacedKey @NonNull [] keys, int @NonNull [] amounts) {
        this.keys = keys;
        this.amounts = amounts;
    }

    public int[] getAmounts() {
        return amounts.clone();
    }

    public boolean matches(@NonNull CraftMatrix that) {
        if (!Arrays.equals(this.keys, that.keys)) {
            return false;
        }
        for (int i = 0; i < 9; i++) {
            if (this.amounts[i] <= that.amounts[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(keys);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CraftMatrix that))
            return false;
        return matches(that);
    }
}
