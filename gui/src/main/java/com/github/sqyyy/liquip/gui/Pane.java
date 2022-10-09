package com.github.sqyyy.liquip.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public interface Pane {
    default void initialize(@NotNull Menu menu) {
    }

    int getPriority();

    boolean collidesWith(int slot);

    default boolean canPutItem(int slot) {
        return false;
    }

    default boolean canTakeItem(int slot) {
        return false;
    }

    default void apply(@NotNull Inventory inventory) {
    }

    default void onPutItem(@NotNull InventoryClickEvent event) {
    }

    default void onTakeItem(@NotNull InventoryClickEvent event) {
    }

    default void onClickItem(@NotNull InventoryClickEvent event) {
    }

    default void onDragItems(@NotNull InventoryDragEvent event) {
    }

    default void onCloseInventory(@NotNull InventoryCloseEvent event) {
    }
}
