package com.github.sqyyy.liquip.gui.impl;

import com.github.sqyyy.liquip.gui.Pane;
import com.github.sqyyy.liquip.gui.Priority;
import com.github.sqyyy.liquip.gui.Slot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FillPane implements Pane {
    private final int priority;
    private final int startRow;
    private final int startColumn;
    private final int endRow;
    private final int endColumn;
    private final ItemStack fillItem;

    public FillPane(int priority, @NotNull Slot from, @NotNull Slot to, @Nullable ItemStack fillItem) {
        this(priority, from.getRow(), from.getColumn(), to.getRow(), to.getColumn(), fillItem);
    }

    public FillPane(int priority, int startRow, int startColumn, int endRow, int endColumn,
                    @Nullable ItemStack fillItem) {
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
        this.fillItem = fillItem;
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

    public @Nullable ItemStack getFillItem() {
        return fillItem;
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
    public void apply(@NotNull Inventory inventory) {
        for (int row = startRow; row <= endRow; row++) {
            for (int column = startColumn; column <= endColumn; column++) {
                inventory.setItem(row * 9 + column, fillItem);
            }
        }
    }
}
