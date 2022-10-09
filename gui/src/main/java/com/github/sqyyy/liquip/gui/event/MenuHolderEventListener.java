package com.github.sqyyy.liquip.gui.event;

import com.github.sqyyy.liquip.gui.MenuHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;

public class MenuHolderEventListener implements Listener {
    @EventHandler
    public void onClick(@NotNull InventoryClickEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof MenuHolder holder) {
            holder.onClickItem(event);
        }
    }

    @EventHandler
    public void onClose(@NotNull InventoryCloseEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof MenuHolder holder) {
            holder.onCloseInventory(event);
        }
    }

    @EventHandler
    public void onDrag(@NotNull InventoryDragEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof MenuHolder holder) {
            holder.onDragItems(event);
        }
    }
}
