package io.github.liquip.api.item;

import net.kyori.adventure.key.Keyed;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public interface Enchantment extends Keyed {
    default int maxLevel() {
        return 1;
    }

    default boolean canApply(@NotNull ItemStack itemStack) {
        return true;
    }
}
