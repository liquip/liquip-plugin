package com.github.sqyyy.liquip.example.util;

import org.bukkit.block.Block;

public class BlockRelative {
    public static final BlockRelative UP = new BlockRelative(0, 1, 0);
    public static final BlockRelative DOWN = new BlockRelative(0, -1, 0);
    public static final BlockRelative NORTH = new BlockRelative(0, 0, -1);
    public static final BlockRelative EAST = new BlockRelative(1, 0, 0);
    public static final BlockRelative SOUTH = new BlockRelative(0, 0, 1);
    public static final BlockRelative WEST = new BlockRelative(-1, 0, 0);
    public static final BlockRelative UP_NORTH = new BlockRelative(0, 1, -1);
    public static final BlockRelative UP_EAST = new BlockRelative(1, 1, 0);
    public static final BlockRelative UP_SOUTH = new BlockRelative(0, 1, 1);
    public static final BlockRelative UP_WEST = new BlockRelative(-1, 1, 0);
    public static final BlockRelative DOWN_NORTH = new BlockRelative(0, -1, -1);
    public static final BlockRelative DOWN_EAST = new BlockRelative(1, -1, 0);
    public static final BlockRelative DOWN_SOUTH = new BlockRelative(0, -1, 1);
    public static final BlockRelative DOWN_WEST = new BlockRelative(-1, -1, 0);
    public static final BlockRelative NORTH_EAST = new BlockRelative(1, 0, -1);
    public static final BlockRelative NORTH_WEST = new BlockRelative(-1, 0, -1);
    public static final BlockRelative SOUTH_EAST = new BlockRelative(1, 0, 1);
    public static final BlockRelative SOUTH_WEST = new BlockRelative(-1, 0, 1);
    public static final BlockRelative UP_NORTH_EAST = new BlockRelative(1, 1, -1);
    public static final BlockRelative UP_NORTH_WEST = new BlockRelative(-1, 1, -1);
    public static final BlockRelative UP_SOUTH_EAST = new BlockRelative(1, 1, 1);
    public static final BlockRelative UP_SOUTH_WEST = new BlockRelative(-1, 1, 1);
    public static final BlockRelative DOWN_NORTH_EAST = new BlockRelative(1, -1, -1);
    public static final BlockRelative DOWN_NORTH_WEST = new BlockRelative(-1, -1, -1);
    public static final BlockRelative DOWN_SOUTH_EAST = new BlockRelative(1, -1, 1);
    public static final BlockRelative DOWN_SOUTH_WEST = new BlockRelative(-1, -1, 1);
    private static final BlockRelative[] VALUES =
            new BlockRelative[]{UP, DOWN, NORTH, EAST, SOUTH, WEST, UP_NORTH, UP_EAST, UP_SOUTH, UP_WEST, DOWN_NORTH, DOWN_EAST,
                    DOWN_SOUTH, DOWN_WEST, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST, UP_NORTH_EAST, UP_NORTH_WEST,
                    UP_SOUTH_EAST, UP_SOUTH_WEST, DOWN_NORTH_EAST, DOWN_NORTH_WEST, DOWN_SOUTH_EAST, DOWN_SOUTH_WEST};

    private final int plusX;
    private final int plusY;
    private final int plusZ;

    public static BlockRelative[] values() {
        return VALUES.clone();
    }

    public BlockRelative(int plusX, int plusY, int plusZ) {
        this.plusX = plusX;
        this.plusY = plusY;
        this.plusZ = plusZ;
    }

    public Block getRelative(Block block) {
        return block.getRelative(plusX, plusY, plusZ);
    }
}
