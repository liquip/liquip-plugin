package com.github.sqyyy.liquip.gui.impl;

import com.github.sqyyy.liquip.gui.Pane;
import com.github.sqyyy.liquip.gui.Priority;
import com.github.sqyyy.liquip.gui.Slot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FillItemPane implements Pane {
    private final int priority;
    private final int row;
    private final int column;
    private final ItemStack fillItem;

    public FillItemPane(int priority, @NotNull Slot slot, @Nullable ItemStack fillItem) {
        this(priority, slot.getRow(), slot.getColumn(), fillItem);
    }

    public FillItemPane(int priority, int row, int column, @Nullable ItemStack fillItem) {
        if (priority < Priority.MIN_PRIORITY || priority > Priority.MAX_PRIORITY) {
            throw new IllegalArgumentException(
                    "priority must be Priority.MIN_PRIORITY < priority < Priority.MAX_PRIORITY");
        }
        if (row > 5) {
            throw new IllegalArgumentException("startColumn > 5");
        }
        if (column > 8) {
            throw new IllegalArgumentException("column > 8");
        }
        this.priority = priority;
        this.row = row;
        this.column = column;
        this.fillItem = fillItem;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
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
        return slot == row * 9 + column;
    }

    @Override
    public void apply(@NotNull Inventory inventory) {
        inventory.setItem(row * 9 + column, fillItem);
    }
}
