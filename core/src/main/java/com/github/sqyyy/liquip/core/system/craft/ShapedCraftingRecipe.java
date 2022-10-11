package com.github.sqyyy.liquip.core.system.craft;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.util.Identifier;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ShapedCraftingRecipe implements CraftingRecipe {
    private final int[] counts;
    private final Identifier[] identifiers;
    private final Identifier resultIdentifier;

    public ShapedCraftingRecipe(int @NotNull [] counts, @NotNull Identifier @NotNull [] identifiers,
                                @NotNull Identifier resultIdentifier) {
        if (counts.length != 9) {
            throw new IllegalArgumentException("counts.length != 9");
        }
        if (identifiers.length != 9) {
            throw new IllegalArgumentException("identifiers.length != 9");
        }
        this.counts = counts;
        this.identifiers = identifiers;
        this.resultIdentifier = resultIdentifier;
    }

    @Override
    public boolean matches(@NotNull ItemStack @NotNull [] grid, @NotNull Identifier @NotNull [] idGrid) {
        if (grid.length != 9) {
            return false;
        }
        if (idGrid.length != 9) {
            return false;
        }
        for (int i = 0; i < 9; i++) {
            if (counts[i] > grid[i].getAmount()) {
                return false;
            }
            if (!identifiers[i].equals(idGrid[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getResult() {
        return Liquip.getProvider().getItemRegistry().get(resultIdentifier).newItem();
    }

    @Override
    public void craft(@NotNull ItemStack @NotNull [] grid, @NotNull InventoryClickEvent event) {
        final Inventory topInventory = event.getView().getTopInventory();
        if (event.isShiftClick()) {
            // TODO - implement shift-click support
            for (int row = 0; row < 3; row++) {
                for (int column = 0; column < 3; column++) {
                    final ItemStack item = topInventory.getItem((row + 1) * 9 + column + 1);
                    if (item == null || item.getType() == Material.AIR) {
                        continue;
                    }
                    item.setAmount(item.getAmount() - counts[row * 3 + column]);
                }
            }
            return;
        }
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                final ItemStack item = topInventory.getItem((row + 1) * 9 + column + 1);
                if (item == null || item.getType() == Material.AIR) {
                    continue;
                }
                item.setAmount(item.getAmount() - counts[row * 3 + column]);
            }
        }
    }

    public static class Placeholder {
        private final char key;
        private final Identifier type;
        private final int count;

        public Placeholder(char key, @NotNull Identifier type, int count) {
            this.key = key;
            this.type = type;
            this.count = count;
        }

        public char getKey() {
            return key;
        }

        public @NotNull Identifier getType() {
            return type;
        }

        public int getCount() {
            return count;
        }
    }
}
