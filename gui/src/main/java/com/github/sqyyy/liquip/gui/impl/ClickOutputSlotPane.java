package com.github.sqyyy.liquip.gui.impl;

import com.github.sqyyy.liquip.gui.Pane;
import com.github.sqyyy.liquip.gui.Slot;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ClickOutputSlotPane implements Pane {
    private final int priority;
    private final int row;
    private final int column;
    private final Function<InventoryClickEvent, ItemStack> itemStackSupplier;

    public ClickOutputSlotPane(int priority, @NotNull Slot slot,
                               @NotNull Function<InventoryClickEvent, ItemStack> itemStackSupplier) {
        this(priority, slot.getRow(), slot.getColumn(), itemStackSupplier);
    }

    public ClickOutputSlotPane(int priority, int row, int column,
                               @NotNull Function<InventoryClickEvent, ItemStack> itemStackSupplier) {
        this.priority = priority;
        this.row = row;
        this.column = column;
        this.itemStackSupplier = itemStackSupplier;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean collidesWith(int slot) {
        return slot == row * 9 + column;
    }

    @Override
    public void apply(@NotNull Inventory inventory) {
        inventory.setItem(row * 9 + column, null);
    }

    @Override
    public void onClickItem(@NotNull InventoryClickEvent event) {
        if (event.getRawSlot() != row * 9 + column) {
            return;
        }
        final HumanEntity player = event.getWhoClicked();
        final PlayerInventory inventory = player.getInventory();
        final int firstEmpty = inventory.firstEmpty();
        if (firstEmpty == -1) {
            return;
        }
        final ItemStack itemStack = itemStackSupplier.apply(event);
        inventory.setItem(firstEmpty, itemStack);
    }
}
