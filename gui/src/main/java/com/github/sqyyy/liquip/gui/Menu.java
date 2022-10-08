package com.github.sqyyy.liquip.gui;

import com.github.sqyyy.liquip.gui.event.MenuHolderEventListener;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface Menu {
    Component getTitle();

    int getRows();

    MenuType getType();

    List<Pane> getPanes(int priority);

    int nextPutSlot(int fromSlot, Inventory inventory);

    default boolean canPutItem(int slot) {
        return false;
    }

    default boolean canTakeItem(int slot) {
        return false;
    }

    void open(Player player);

    void registerEventListener(MenuEventListener listener);

    void onClickItem(InventoryClickEvent event);

    void onCloseInventory(InventoryCloseEvent event);

    void onDragItems(InventoryDragEvent event);

    static void initialize(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new MenuHolderEventListener(), plugin);
    }
}
