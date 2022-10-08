package com.github.sqyyy.liquip.gui.impl;

import com.github.sqyyy.liquip.gui.Menu;
import com.github.sqyyy.liquip.gui.MenuHolder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class BasicMenuHolder implements MenuHolder {
    private final Menu menu;
    private Inventory inventory = null;

    public BasicMenuHolder(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    @Override
    public void onClickItem(InventoryClickEvent event) {
        menu.onClickItem(event);
    }

    @Override
    public void onCloseInventory(InventoryCloseEvent event) {
        menu.onCloseInventory(event);
    }

    @Override
    public void onDragItems(InventoryDragEvent event) {
        menu.onDragItems(event);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        if (inventory.getHolder() != this) {
            throw new IllegalArgumentException("inventory has wrong holder");
        }
        this.inventory = inventory;
    }
}
