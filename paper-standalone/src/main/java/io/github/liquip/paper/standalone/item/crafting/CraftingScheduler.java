package io.github.liquip.paper.standalone.item.crafting;

import com.github.sqyyy.jcougar.Slot;
import io.github.liquip.api.item.crafting.Recipe;
import io.github.liquip.paper.standalone.StandaloneLiquipImpl;
import net.kyori.adventure.key.KeyedValue;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CraftingScheduler extends BukkitRunnable {
    private final StandaloneLiquipImpl api;
    private final InventoryView view;

    public CraftingScheduler(@NonNull StandaloneLiquipImpl api, @NonNull InventoryView view) {
        this.api = api;
        this.view = view;
    }

    @Override
    public void run() {
        if (this.view != this.view.getPlayer().getOpenInventory()) {
            return;
        }
        final Inventory topInventory = this.view.getTopInventory();
        final List<KeyedValue<Integer>> stacks = new ArrayList<>(Collections.nCopies(9, null));
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                final ItemStack item = topInventory.getItem((row + 1) * 9 + column + 1);
                if (item == null || item.getType() == Material.AIR) {
                    stacks.set(row * 3 + column, null);
                    continue;
                }
                stacks.set(row * 3 + column, KeyedValue.keyedValue(this.api.getKeyFromItemStack(item), item.getAmount()));
            }
        }
        final UnboundCraftMatrixImpl craftMatrix = new UnboundCraftMatrixImpl(true, stacks);
        final Recipe shapedRecipe = this.api.getCraftingSystem().getShapedRecipe(craftMatrix);
        if (shapedRecipe != null) {
            final ItemStack result = shapedRecipe.getResult(stacks);
            topInventory.setItem(Slot.RowThreeSlotSeven.chestSlot, result);
            return;
        }
        craftMatrix.setShaped(false);
        final Recipe shapelessRecipe = this.api.getCraftingSystem().getShapelessRecipe(craftMatrix);
        if (shapelessRecipe != null) {
            final ItemStack result = shapelessRecipe.getResult(stacks);
            topInventory.setItem(Slot.RowThreeSlotSeven.chestSlot, result);
            return;
        }
        topInventory.setItem(Slot.RowThreeSlotSeven.chestSlot, null);
    }
}
