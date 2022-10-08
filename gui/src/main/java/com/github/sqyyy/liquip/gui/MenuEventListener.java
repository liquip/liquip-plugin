package com.github.sqyyy.liquip.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public interface MenuEventListener {
    default void onClickItem(InventoryClickEvent event) {
    }

    default void onCloseInventory(InventoryCloseEvent event) {
    }

    default void onDragItems(InventoryDragEvent event) {
    }
}
