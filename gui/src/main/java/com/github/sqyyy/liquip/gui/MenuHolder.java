package com.github.sqyyy.liquip.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

public interface MenuHolder extends InventoryHolder {
    void onClickItem(InventoryClickEvent event);

    void onCloseInventory(InventoryCloseEvent event);

    void onDragItems(InventoryDragEvent event);
}
