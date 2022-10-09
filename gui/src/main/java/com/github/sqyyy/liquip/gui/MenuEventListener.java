package com.github.sqyyy.liquip.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;

public interface MenuEventListener {
    default void onClickItem(@NotNull InventoryClickEvent event) {
    }

    default void onCloseInventory(@NotNull InventoryCloseEvent event) {
    }

    default void onDragItems(@NotNull InventoryDragEvent event) {
    }
}
