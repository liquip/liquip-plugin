package com.github.sqyyy.liquip.gui.impl;

import com.github.sqyyy.liquip.gui.Pane;
import com.github.sqyyy.liquip.gui.Priority;
import com.github.sqyyy.liquip.gui.Slot;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.function.BiConsumer;

public class StoragePane implements Pane {
    private final int priority;
    private final int startRow;
    private final int startColumn;
    private final int endRow;
    private final int endColumn;
    private final BiConsumer<StoragePane, Inventory> setup;
    private final BiConsumer<StoragePane, InventoryCloseEvent> onClose;

    public StoragePane(int priority, Slot from, Slot to, BiConsumer<StoragePane, Inventory> setup,
                       BiConsumer<StoragePane, InventoryCloseEvent> onClose) {
        this(priority, from.getRow(), from.getColumn(), to.getRow(), to.getColumn(), setup, onClose);
    }

    public StoragePane(int priority, int startRow, int startColumn, int endRow, int endColumn,
                       BiConsumer<StoragePane, Inventory> setup, BiConsumer<StoragePane, InventoryCloseEvent> onClose) {
        if (priority < Priority.MIN_PRIORITY || priority > Priority.MAX_PRIORITY) {
            throw new IllegalArgumentException(
                    "priority must be Priority.MIN_PRIORITY < priority < Priority.MAX_PRIORITY");
        }
        if (startRow > endRow) {
            throw new IllegalArgumentException("startRow > endRow");
        }
        if (startColumn > endColumn) {
            throw new IllegalArgumentException("startColumn > endColumn");
        }
        this.priority = priority;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.endRow = endRow;
        this.endColumn = endColumn;
        this.setup = setup;
        this.onClose = onClose;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getEndColumn() {
        return endColumn;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean collidesWith(int slot) {
        final int row = Slot.getRow(slot);
        final int column = Slot.getColumn(slot);
        return (row >= this.startRow && row <= this.endRow) && (column >= this.startColumn && column <= this.endColumn);
    }

    @Override
    public boolean canPutItem(int slot) {
        final int row = Slot.getRow(slot);
        final int column = Slot.getColumn(slot);
        return (row >= this.startRow && row <= this.endRow) && (column >= this.startColumn && column <= this.endColumn);
    }

    @Override
    public boolean canTakeItem(int slot) {
        final int row = Slot.getRow(slot);
        final int column = Slot.getColumn(slot);
        return (row >= this.startRow && row <= this.endRow) && (column >= this.startColumn && column <= this.endColumn);
    }

    @Override
    public void apply(Inventory inventory) {
        for (int row = startRow; row <= endRow; row++) {
            for (int column = startColumn; column <= endColumn; column++) {
                inventory.setItem(row * 9 + column, null);
            }
        }
        setup.accept(this, inventory);
    }

    @Override
    public void onCloseInventory(InventoryCloseEvent event) {
        onClose.accept(this, event);
    }
}
