package io.github.liquip.paper.standalone.item.crafting;

import com.github.sqyyy.liquip.gui.Slot;
import io.github.liquip.paper.standalone.StandaloneLiquipImpl;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class CraftingUpdateScheduler extends BukkitRunnable {
    private final StandaloneLiquipImpl api;
    private final InventoryView view;
    private ItemStack[] grid;
    private NamespacedKey[] idGrid;

    public CraftingUpdateScheduler(@NonNull StandaloneLiquipImpl api, @NonNull InventoryView view) {
        this.api = api;
        this.view = view;
    }

    public InventoryView getView() {
        return view;
    }

    public @NotNull ItemStack @NotNull [] getGrid() {
        return grid;
    }

    public @NotNull NamespacedKey @NotNull [] getIdGrid() {
        return idGrid;
    }

    private void load() {
        final Inventory topInventory = view.getTopInventory();
        grid = new ItemStack[9];
        idGrid = new NamespacedKey[9];
        final ItemStack air = new ItemStack(Material.AIR);
        final NamespacedKey airId = new NamespacedKey("minecraft", "air");
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                final ItemStack item = topInventory.getItem((row + 1) * 9 + column + 1);
                if (item == null || item.getType() == Material.AIR) {
                    grid[row * 3 + column] = air;
                    idGrid[row * 3 + column] = airId;
                    continue;
                }
                grid[row * 3 + column] = item;
                idGrid[row * 3 + column] = api.getKeyFromItemStack(item);
            }
        }
    }

    @Override
    public void run() {
        load();
        final Inventory topInventory = view.getTopInventory();
        final CraftingHashObject craftingHashObject = new CraftingHashObject(grid, true);
        final CraftingRegistry registry = api.getCraftingRegistry();
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
