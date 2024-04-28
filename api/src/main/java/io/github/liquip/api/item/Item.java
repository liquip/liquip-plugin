package io.github.liquip.api.item;

import net.kyori.adventure.key.Keyed;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A custom item managing the {@link ItemStack} instances created by it.
 *
 * @since 0.0.1-alpha
 */
public interface Item extends Keyed {
    /**
     * Creates a new item stack of itself.
     *
     * <p>The item stack will hold all information necessary to identify that it belongs to this object.</p>
     *
     * @return a new item stack of itself
     * @since 0.0.1-alpha
     */
    @NotNull
    ItemStack newItemStack();
}
