package com.github.sqyyy.liquip.gui.impl;

import com.github.sqyyy.liquip.gui.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BasicMenu implements Menu {
    private final Component title;
    private final int rows;
    private final MenuType type;
    private final List<Pane>[] panes;
    private final boolean[] putSlots;
    private final boolean[] takeSlots;
    private final List<MenuEventListener> listeners;

    @SuppressWarnings("unchecked")
    public BasicMenu(@NotNull Component title, int rows, @NotNull MenuType type, @NotNull List<@NotNull Pane> panes) {
        switch (type) {
            case CHEST -> {
                if (rows > 6 || rows < 1) {
                    throw new IllegalArgumentException("rows is not in range 1..6");
                }
                putSlots = new boolean[9 * rows];
                takeSlots = new boolean[9 * rows];
                Arrays.fill(putSlots, false);
                Arrays.fill(takeSlots, false);
            }
            case HOPPER -> {
                if (rows != 1) {
                    throw new IllegalArgumentException("rows is not 1");
                }
                putSlots = new boolean[5];
                takeSlots = new boolean[5];
                Arrays.fill(putSlots, false);
                Arrays.fill(takeSlots, false);
            }
            case DISPENSER -> {
                if (rows != 3) {
                    throw new IllegalArgumentException("rows is not 3");
                }
                putSlots = new boolean[9];
                takeSlots = new boolean[9];
                Arrays.fill(putSlots, false);
                Arrays.fill(takeSlots, false);
            }
            default -> throw new IllegalArgumentException();
        }
        final List<Pane>[] orderedPanes = new List[Priority.MAX_PRIORITY + 1];
        for (int i = 0; i < orderedPanes.length; i++) {
            orderedPanes[i] = new ArrayList<>(1);
        }
        for (Pane pane : panes) {
            final int priority = pane.getPriority();
            if (priority > Priority.MAX_PRIORITY) {
                throw new IllegalArgumentException("pane.getPriority() > Priority.MAX_PRIORITY");
            }
            orderedPanes[priority].add(pane);
        }
        for (int i = 0; i < putSlots.length; i++) {
            for (List<Pane> paneList : orderedPanes) {
                for (Pane pane : paneList) {
                    if (pane.collidesWith(i)) {
                        putSlots[i] = pane.canPutItem(i);
                        takeSlots[i] = pane.canTakeItem(i);
                    }
                }
            }
        }
        this.title = title;
        this.rows = rows;
        this.type = type;
        this.panes = orderedPanes;
        listeners = new ArrayList<>(1);
    }

    public BasicMenu(@NotNull Component title, int rows, @NotNull MenuType type,
                     @NotNull List<@NotNull Pane> @NotNull [] panes) {
        if (panes.length > Priority.MAX_PRIORITY) {
            throw new IllegalArgumentException("panes.length > Priority.MAX_PRIORITY");
        }
        switch (type) {
            case CHEST -> {
                if (rows > 6 || rows < 1) {
                    throw new IllegalArgumentException("rows is not in range 1..6");
                }
                putSlots = new boolean[9 * rows];
                takeSlots = new boolean[9 * rows];
                Arrays.fill(putSlots, false);
                Arrays.fill(takeSlots, false);
            }
            case HOPPER -> {
                if (rows != 1) {
                    throw new IllegalArgumentException("rows is not 1");
                }
                putSlots = new boolean[5];
                takeSlots = new boolean[5];
                Arrays.fill(putSlots, false);
                Arrays.fill(takeSlots, false);
            }
            case DISPENSER -> {
                if (rows != 3) {
                    throw new IllegalArgumentException("rows is not 3");
                }
                putSlots = new boolean[9];
                takeSlots = new boolean[9];
                Arrays.fill(putSlots, false);
                Arrays.fill(takeSlots, false);
            }
            default -> throw new IllegalArgumentException();
        }
        for (int i = 0; i < putSlots.length; i++) {
            for (List<Pane> paneList : panes) {
                for (Pane pane : paneList) {
                    if (pane.collidesWith(i)) {
                        putSlots[i] = pane.canPutItem(i);
                        takeSlots[i] = pane.canTakeItem(i);
                    }
                }
            }
        }
        this.title = title;
        this.rows = rows;
        this.type = type;
        this.panes = panes;
        listeners = new ArrayList<>(1);
    }

    @Override
    public @NotNull Component getTitle() {
        return title;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public @NotNull MenuType getType() {
        return type;
    }

    @Override
    public @NotNull List<@NotNull Pane> getPanes(int priority) {
        return panes[priority];
    }

    @Override
    public int nextPutSlot(int fromSlot, @NotNull Inventory inventory) {
        if (fromSlot > putSlots.length) {
            return -1;
        }
        for (int i = fromSlot; i < putSlots.length; i++) {
            if (!putSlots[i]) {
                continue;
            }
            final ItemStack itemStack = inventory.getItem(i);
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean canPutItem(int slot) {
        return putSlots[slot];
    }

    @Override
    public boolean canTakeItem(int slot) {
        return takeSlots[slot];
    }

    @Override
    public void open(@NotNull Player player) {
        final BasicMenuHolder holder = new BasicMenuHolder(this);
        final Inventory inventory = switch (type) {
            case CHEST -> Bukkit.createInventory(holder, rows * 9, title);
            case HOPPER -> Bukkit.createInventory(holder, InventoryType.HOPPER, title);
            case DISPENSER -> Bukkit.createInventory(holder, InventoryType.DISPENSER, title);
        };
        holder.setInventory(inventory);
        for (List<Pane> paneList : panes) {
            for (Pane pane : paneList) {
                pane.apply(inventory);
            }
        }
        player.openInventory(inventory);
    }

    @Override
    public void registerEventListener(@NotNull MenuEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onClickItem(@NotNull InventoryClickEvent event) {
        for (MenuEventListener listener : listeners) {
            listener.onClickItem(event);
        }
        final Inventory clickedInventory = event.getClickedInventory();
        final Inventory topInventory = event.getView().getTopInventory();
        final Inventory bottomInventory = event.getView().getBottomInventory();
        final int rawSlot = event.getRawSlot();
        switch (event.getAction()) {
            case NOTHING -> {
            }
            case PICKUP_ALL, PICKUP_HALF, PICKUP_SOME, PICKUP_ONE, DROP_ALL_SLOT, DROP_ONE_SLOT -> {
                if (clickedInventory == bottomInventory) {
                    break;
                }
                if (!takeSlots[rawSlot]) {
                    event.setCancelled(true);
                    callClickItem(event);
                    break;
                }
                callTakeItem(event);
            }
            case PLACE_ALL, PLACE_SOME, PLACE_ONE -> {
                if (clickedInventory == bottomInventory) {
                    break;
                }
                if (!putSlots[rawSlot]) {
                    event.setCancelled(true);
                    callClickItem(event);
                    break;
                }
                callPutItem(event);
            }
            case SWAP_WITH_CURSOR -> {
                if (clickedInventory == bottomInventory) {
                    break;
                }
                if (!putSlots[rawSlot]) {
                    event.setCancelled(true);
                    callClickItem(event);
                    break;
                }
                callPutTakeItem(event);
            }
            case MOVE_TO_OTHER_INVENTORY -> {
                if (clickedInventory == bottomInventory) {
                    final ItemStack item = event.getCurrentItem();
                    if (item == null) {
                        callClickItem(event);
                        break;
                    }
                    final int nextSlot = nextPutSlot(0, topInventory);
                    if (nextSlot == -1) {
                        event.setCancelled(true);
                        callClickItem(event);
                        break;
                    }
                    event.setCancelled(true);
                    topInventory.setItem(nextSlot, item.clone());
                    clickedInventory.setItem(event.getSlot(), null);
                    callPutItem(event);
                } else {
                    if (!takeSlots[rawSlot]) {
                        event.setCancelled(true);
                        callClickItem(event);
                        break;
                    }
                    callTakeItem(event);
                }
            }
            case COLLECT_TO_CURSOR -> event.setCancelled(true);
            case HOTBAR_SWAP -> {
                if (clickedInventory == bottomInventory) {
                    break;
                }
                final ItemStack itemStack = event.getCurrentItem();
                final boolean wasPut = itemStack == null || itemStack.getType() == Material.AIR;
                if (wasPut) {
                    if (!putSlots[rawSlot]) {
                        event.setCancelled(true);
                        callClickItem(event);
                        break;
                    }
                    callPutItem(event);
                    break;
                }
                if (!takeSlots[rawSlot]) {
                    event.setCancelled(true);
                    callClickItem(event);
                    break;
                }
                callTakeItem(event);
            }
            case HOTBAR_MOVE_AND_READD -> {
                if (clickedInventory == bottomInventory) {
                    break;
                }
                if (!takeSlots[rawSlot] || !putSlots[rawSlot]) {
                    event.setCancelled(true);
                    callClickItem(event);
                    break;
                }
                callPutTakeItem(event);
            }
        }
    }

    private void callClickItem(InventoryClickEvent event) {
        for (List<Pane> paneList : panes) {
            for (Pane pane : paneList) {
                if (pane.collidesWith(event.getRawSlot())) {
                    pane.onClickItem(event);
                }
            }
        }
    }

    private void callPutItem(InventoryClickEvent event) {
        for (List<Pane> paneList : panes) {
            for (Pane pane : paneList) {
                if (pane.collidesWith(event.getRawSlot())) {
                    pane.onPutItem(event);
                }
            }
        }
    }

    private void callTakeItem(InventoryClickEvent event) {
        for (List<Pane> paneList : panes) {
            for (Pane pane : paneList) {
                if (pane.collidesWith(event.getRawSlot())) {
                    pane.onTakeItem(event);
                }
            }
        }
    }

    private void callPutTakeItem(InventoryClickEvent event) {
        for (List<Pane> paneList : panes) {
            for (Pane pane : paneList) {
                if (pane.collidesWith(event.getRawSlot())) {
                    pane.onTakeItem(event);
                    pane.onPutItem(event);
                }
            }
        }
    }

    @Override
    public void onCloseInventory(@NotNull InventoryCloseEvent event) {
        for (MenuEventListener listener : listeners) {
            listener.onCloseInventory(event);
        }
        for (List<Pane> paneList : panes) {
            for (Pane pane : paneList) {
                pane.onCloseInventory(event);
            }
        }
    }

    @Override
    public void onDragItems(@NotNull InventoryDragEvent event) {
        final int length = putSlots.length;
        for (int rawSlot : event.getRawSlots()) {
            if (rawSlot >= length) {
                continue;
            }
            if (!putSlots[rawSlot]) {
                event.setCancelled(true);
                for (MenuEventListener listener : listeners) {
                    listener.onDragItems(event);
                }
                return;
            }
        }
        for (List<Pane> paneList : panes) {
            for (Pane pane : paneList) {
                pane.onDragItems(event);
            }
        }
    }
}
