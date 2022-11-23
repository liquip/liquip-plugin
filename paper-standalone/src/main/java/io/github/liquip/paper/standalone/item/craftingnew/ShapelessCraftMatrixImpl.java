package io.github.liquip.paper.standalone.item.craftingnew;

import io.github.liquip.api.item.crafting.CraftMatrix;
import io.github.liquip.api.item.crafting.ShapelessCraftMatrix;
import net.kyori.adventure.key.KeyedValue;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.ApiStatus;

import java.util.Set;

@ApiStatus.Experimental
public class ShapelessCraftMatrixImpl implements ShapelessCraftMatrix {
    private final Set<KeyedValue<Integer>> keys;

    public ShapelessCraftMatrixImpl(Set<KeyedValue<Integer>> keys) {
        this.keys = keys;
    }

    @Override
    public boolean matches(@NonNull CraftMatrix that) {
        return false;
    }
}
