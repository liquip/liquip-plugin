package com.github.sqyyy.liquip.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public interface MenuHolder extends InventoryHolder {
    void onClickItem(@NotNull InventoryClickEvent event);

    void onCloseInventory(@NotNull InventoryCloseEvent event);

    void onDragItems(@NotNull InventoryDragEvent event);
}
