package com.github.sqyyy.liquip.gui.impl;

import com.github.sqyyy.liquip.gui.MenuEventListener;
import com.github.sqyyy.liquip.gui.Pane;
import com.github.sqyyy.liquip.gui.Priority;
import com.github.sqyyy.liquip.gui.Slot;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;

public abstract class ListenerPane implements Pane {
    private final int priority;
    private final int startRow;
    private final int startColumn;
    private final int endRow;
    private final int endColumn;
    private final MenuEventListener listener;

    public ListenerPane(int priority, @NotNull Slot from, @NotNull Slot to, @NotNull MenuEventListener listener) {
        this(priority, from.getRow(), from.getColumn(), to.getRow(), to.getColumn(), listener);
    }

    public ListenerPane(int priority, int startRow, int startColumn, int endRow, int endColumn,
                        @NotNull MenuEventListener listener) {
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
        this.listener = listener;
        this.priority = priority;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.endRow = endRow;
        this.endColumn = endColumn;
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

    public @NotNull MenuEventListener getListener() {
        return listener;
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
    public abstract void onPutItem(@NotNull InventoryClickEvent event);

    @Override
    public abstract void onTakeItem(@NotNull InventoryClickEvent event);

    @Override
    public abstract void onClickItem(@NotNull InventoryClickEvent event);

    @Override
    public abstract void onDragItems(@NotNull InventoryDragEvent event);

    @Override
    public abstract void onCloseInventory(@NotNull InventoryCloseEvent event);
}
