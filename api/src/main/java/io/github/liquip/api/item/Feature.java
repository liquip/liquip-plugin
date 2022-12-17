package io.github.liquip.api.item;

import org.bukkit.Keyed;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A feature that can be applied to {@link Item} instances and the {@link ItemStack} instances created by them.
 */
public interface Feature extends Keyed {
    /**
     * A method that is called for each creation of {@link Item} with this feature.
     *
     * @param item the item that was created
     * @since 1.0.0-beta
     */
    default void initialize(@NonNull Item item) {
    }

    /**
     * A method that is called for each creation of an {@link ItemStack} by an instance of {@link Item} with this feature.
     *
     * @param item the item instance
     * @param itemStack the item stack that was created
     * @since 1.0.0-beta
     */
    default void apply(@NonNull Item item, @NonNull ItemStack itemStack) {
    }
}
