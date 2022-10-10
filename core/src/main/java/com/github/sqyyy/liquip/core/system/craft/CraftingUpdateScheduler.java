package com.github.sqyyy.liquip.core.system.craft;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.core.util.Identifier;
import com.github.sqyyy.liquip.gui.Slot;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class CraftingUpdateScheduler extends BukkitRunnable {
    private final InventoryView view;
    private ItemStack[] grid;
    private Identifier[] idGrid;

    public CraftingUpdateScheduler(@NotNull InventoryView view) {
        this.view = view;
    }

    public InventoryView getView() {
        return view;
    }

    public @NotNull ItemStack @NotNull [] getGrid() {
        return grid;
    }

    public @NotNull Identifier @NotNull [] getIdGrid() {
        return idGrid;
    }

    private void load() {
        final Inventory topInventory = view.getTopInventory();
        grid = new ItemStack[9];
        idGrid = new Identifier[9];
        final ItemStack air = new ItemStack(Material.AIR);
        final Identifier airId = new Identifier("minecraft", "air");
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                final ItemStack item = topInventory.getItem((row + 1) * 9 + column + 1);
                if (item == null || item.getType() == Material.AIR) {
                    grid[row * 3 + column] = air;
                    idGrid[row * 3 + column] = airId;
                    continue;
                }
                grid[row * 3 + column] = item;
                idGrid[row * 3 + column] = LiquipItem.getIdentifier(item);
            }
        }
    }

    @Override
    public void run() {
        load();
        final Inventory topInventory = view.getTopInventory();
        final CraftingHashObject craftingHashObject = new CraftingHashObject(grid, true);
        final CraftingRegistry registry = Liquip.getProvider().getCraftingRegistry();
        if (registry.isRegistered(craftingHashObject)) {
            final CraftingRecipe recipe = registry.get(craftingHashObject);
            if (!recipe.matches(grid, idGrid)) {
                topInventory.setItem(Slot.ROW_THREE_SLOT_SEVEN.getSlot(), null);
                return;
            }
            topInventory.setItem(Slot.ROW_THREE_SLOT_SEVEN.getSlot(), recipe.getResult());
            return;
        }
        craftingHashObject.setShaped(false);
        if (!registry.isRegistered(craftingHashObject)) {
            topInventory.setItem(Slot.ROW_THREE_SLOT_SEVEN.getSlot(), null);
            return;
        }
        final CraftingRecipe recipe = registry.get(craftingHashObject);
        if (!recipe.matches(grid, idGrid)) {
            topInventory.setItem(Slot.ROW_THREE_SLOT_SEVEN.getSlot(), null);
            return;
        }
        topInventory.setItem(Slot.ROW_THREE_SLOT_SEVEN.getSlot(), recipe.getResult());
    }
}
