package com.github.sqyyy.liquip.gui.impl;

import com.github.sqyyy.liquip.gui.Pane;
import com.github.sqyyy.liquip.gui.Slot;
import org.bukkit.inventory.Inventory;

public class OutputSlotPane implements Pane {
    private final int priority;
    private final int row;
    private final int column;

    public OutputSlotPane(int priority, Slot slot) {
        this(priority, slot.getRow(), slot.getColumn());
    }

    public OutputSlotPane(int priority, int row, int column) {
        this.priority = priority;
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
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
    public boolean canTakeItem(int slot) {
        return slot == row * 9 + column;
    }

    @Override
    public void apply(Inventory inventory) {
        inventory.setItem(row * 9 + column, null);
    }
}
