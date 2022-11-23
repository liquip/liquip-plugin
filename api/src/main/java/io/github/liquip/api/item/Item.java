package io.github.liquip.api.item;

import net.kyori.adventure.key.Keyed;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.BiConsumer;

public interface Item extends Keyed {
    @NonNull ItemStack newItemStack();

    <T extends Event> void callEvent(@NonNull Class<T> eventClass, @NonNull T event,
                                     @NonNull ItemStack itemStack);

    <T extends Event> void registerEvent(@NonNull Class<T> eventClass,
                                         @NonNull BiConsumer<T, ItemStack> eventHandler);
}
