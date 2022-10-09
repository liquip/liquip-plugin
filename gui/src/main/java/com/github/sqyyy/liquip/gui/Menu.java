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
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Menu {
    static void initialize(@NotNull Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new MenuHolderEventListener(), plugin);
    }

    @NotNull Component getTitle();

    int getRows();

    @NotNull MenuType getType();

    @NotNull List<@NotNull Pane> getPanes(int priority);

    int nextPutSlot(int fromSlot, @NotNull Inventory inventory);

    default boolean canPutItem(int slot) {
        return false;
    }

    default boolean canTakeItem(int slot) {
        return false;
    }

    void open(@NotNull Player player);

    void registerEventListener(@NotNull MenuEventListener listener);

    void onClickItem(@NotNull InventoryClickEvent event);

    void onCloseInventory(@NotNull InventoryCloseEvent event);

    void onDragItems(@NotNull InventoryDragEvent event);
}
