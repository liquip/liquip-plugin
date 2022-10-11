package com.github.sqyyy.liquip.gui;

import org.jetbrains.annotations.NotNull;

public class Slot {
    private static final Slot[][] slots = new Slot[6][9];
    public static final Slot ROW_ONE_SLOT_ONE = new Slot(0, 0);
    public static final Slot ROW_ONE_SLOT_TWO = new Slot(0, 1);
    public static final Slot ROW_ONE_SLOT_THREE = new Slot(0, 2);
    public static final Slot ROW_ONE_SLOT_FOUR = new Slot(0, 3);
    public static final Slot ROW_ONE_SLOT_FIVE = new Slot(0, 4);
    public static final Slot ROW_ONE_SLOT_SIX = new Slot(0, 5);
    public static final Slot ROW_ONE_SLOT_SEVEN = new Slot(0, 6);
    public static final Slot ROW_ONE_SLOT_EIGHT = new Slot(0, 7);
    public static final Slot ROW_ONE_SLOT_NINE = new Slot(0, 8);

    public static final Slot ROW_TWO_SLOT_ONE = new Slot(1, 0);
    public static final Slot ROW_TWO_SLOT_TWO = new Slot(1, 1);
    public static final Slot ROW_TWO_SLOT_THREE = new Slot(1, 2);
    public static final Slot ROW_TWO_SLOT_FOUR = new Slot(1, 3);
    public static final Slot ROW_TWO_SLOT_FIVE = new Slot(1, 4);
    public static final Slot ROW_TWO_SLOT_SIX = new Slot(1, 5);
    public static final Slot ROW_TWO_SLOT_SEVEN = new Slot(1, 6);
    public static final Slot ROW_TWO_SLOT_EIGHT = new Slot(1, 7);
    public static final Slot ROW_TWO_SLOT_NINE = new Slot(1, 8);

    public static final Slot ROW_THREE_SLOT_ONE = new Slot(2, 0);
    public static final Slot ROW_THREE_SLOT_TWO = new Slot(2, 1);
    public static final Slot ROW_THREE_SLOT_THREE = new Slot(2, 2);
    public static final Slot ROW_THREE_SLOT_FOUR = new Slot(2, 3);
    public static final Slot ROW_THREE_SLOT_FIVE = new Slot(2, 4);
    public static final Slot ROW_THREE_SLOT_SIX = new Slot(2, 5);
    public static final Slot ROW_THREE_SLOT_SEVEN = new Slot(2, 6);
    public static final Slot ROW_THREE_SLOT_EIGHT = new Slot(2, 7);
    public static final Slot ROW_THREE_SLOT_NINE = new Slot(2, 8);

    public static final Slot ROW_FOUR_SLOT_ONE = new Slot(3, 0);
    public static final Slot ROW_FOUR_SLOT_TWO = new Slot(3, 1);
    public static final Slot ROW_FOUR_SLOT_THREE = new Slot(3, 2);
    public static final Slot ROW_FOUR_SLOT_FOUR = new Slot(3, 3);
    public static final Slot ROW_FOUR_SLOT_FIVE = new Slot(3, 4);
    public static final Slot ROW_FOUR_SLOT_SIX = new Slot(3, 5);
    public static final Slot ROW_FOUR_SLOT_SEVEN = new Slot(3, 6);
    public static final Slot ROW_FOUR_SLOT_EIGHT = new Slot(3, 7);
    public static final Slot ROW_FOUR_SLOT_NINE = new Slot(3, 8);

    public static final Slot ROW_FIVE_SLOT_ONE = new Slot(4, 0);
    public static final Slot ROW_FIVE_SLOT_TWO = new Slot(4, 1);
    public static final Slot ROW_FIVE_SLOT_THREE = new Slot(4, 2);
    public static final Slot ROW_FIVE_SLOT_FOUR = new Slot(4, 3);
    public static final Slot ROW_FIVE_SLOT_FIVE = new Slot(4, 4);
    public static final Slot ROW_FIVE_SLOT_SIX = new Slot(4, 5);
    public static final Slot ROW_FIVE_SLOT_SEVEN = new Slot(4, 6);
    public static final Slot ROW_FIVE_SLOT_EIGHT = new Slot(4, 7);
    public static final Slot ROW_FIVE_SLOT_NINE = new Slot(4, 8);

    public static final Slot ROW_SIX_SLOT_ONE = new Slot(5, 0);
    public static final Slot ROW_SIX_SLOT_TWO = new Slot(5, 1);
    public static final Slot ROW_SIX_SLOT_THREE = new Slot(5, 2);
    public static final Slot ROW_SIX_SLOT_FOUR = new Slot(5, 3);
    public static final Slot ROW_SIX_SLOT_FIVE = new Slot(5, 4);
    public static final Slot ROW_SIX_SLOT_SIX = new Slot(5, 5);
    public static final Slot ROW_SIX_SLOT_SEVEN = new Slot(5, 6);
    public static final Slot ROW_SIX_SLOT_EIGHT = new Slot(5, 7);
    public static final Slot ROW_SIX_SLOT_NINE = new Slot(5, 8);

    private final int row;
    private final int column;

    private Slot(int row, int column) {
        this.row = row;
        this.column = column;
        slots[row][column] = this;
    }

    public static @NotNull Slot get(int row, int column) {
        return slots[row][column];
    }

    public static int getRow(int slot) {
        return (slot - (slot % 9)) / 9;
    }

    public static int getColumn(int slot) {
        return slot % 9;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getSlot() {
        return row * 9 + column;
    }
}
