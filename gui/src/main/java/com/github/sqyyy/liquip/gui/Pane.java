package com.github.sqyyy.liquip.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public interface Pane {
    default void initialize(Menu menu) {
    }

    int getPriority();

    boolean collidesWith(int slot);

    default boolean canPutItem(int slot) {
        return false;
    }

    default boolean canTakeItem(int slot) {
        return false;
    }

    void apply(Inventory inventory);

    default void onPutItem(InventoryClickEvent event) {
    }

    default void onTakeItem(InventoryClickEvent event) {
    }

    default void onClickItem(InventoryClickEvent event) {
    }

    default void onDragItems(InventoryDragEvent event) {
    }

    default void onCloseInventory(InventoryCloseEvent event) {
    }
}
