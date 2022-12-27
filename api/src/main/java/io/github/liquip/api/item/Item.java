package io.github.liquip.api.item;

import net.kyori.adventure.key.Keyed;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.BiConsumer;

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
    @NonNull ItemStack newItemStack();

    /**
     * Calls registered event handlers of the specified event.
     *
     * @param eventClass the class of the event to be handled
     * @param event      the instance of the event to be handled
     * @param itemStack  the item stack that is associated to this object
     * @param <T>        the type of the event to be handled
     * @since 0.0.1-alpha
     */
    <T extends Event> void callEvent(@NonNull Class<T> eventClass, @NonNull T event, @NonNull ItemStack itemStack);

    /**
     * Registers an event handler for the specified event class.
     *
     * @param eventClass   the class of the event to be registered
     * @param eventHandler the method to handle the event
     * @param <T>          the type of the event to be registered
     * @since 0.0.1-alpha
     */
    <T extends Event> void registerEvent(@NonNull Class<T> eventClass, @NonNull BiConsumer<T, ItemStack> eventHandler);
}
