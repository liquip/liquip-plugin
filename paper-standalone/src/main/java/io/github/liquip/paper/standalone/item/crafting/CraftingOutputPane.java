package io.github.liquip.paper.standalone.item.crafting;

import com.github.sqyyy.liquip.gui.Slot;
import com.github.sqyyy.liquip.gui.impl.OutputSlotPane;
import io.github.liquip.api.item.crafting.Recipe;
import io.github.liquip.paper.standalone.StandaloneLiquipImpl;
import net.kyori.adventure.key.KeyedValue;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CraftingOutputPane extends OutputSlotPane {
    private final StandaloneLiquipImpl api;

    public CraftingOutputPane(@NonNull StandaloneLiquipImpl api, int priority) {
        super(priority, Slot.ROW_THREE_SLOT_SEVEN);
        this.api = api;
    }

    private @Nullable ItemStack @NonNull [] collectItemStacks(@NonNull Inventory inventory) {
        return new ItemStack[]{
            inventory.getItem(10),
            inventory.getItem(11),
            inventory.getItem(12),
            inventory.getItem(19),
            inventory.getItem(20),
            inventory.getItem(21),
            inventory.getItem(28),
            inventory.getItem(29),
            inventory.getItem(30)
        };
    }

    private void applyItemStacks(@NonNull Inventory inventory, @NonNull ItemStack @NonNull [] stacks) {
        inventory.setItem(10, stacks[0]);
        inventory.setItem(11, stacks[1]);
        inventory.setItem(12, stacks[2]);
        inventory.setItem(19, stacks[3]);
        inventory.setItem(20, stacks[4]);
        inventory.setItem(21, stacks[5]);
        inventory.setItem(28, stacks[6]);
        inventory.setItem(29, stacks[7]);
        inventory.setItem(30, stacks[8]);
    }

    @Override
    public void onTakeItem(@NonNull InventoryClickEvent event) {
        if (event.getRawSlot() != Slot.ROW_THREE_SLOT_SEVEN.getSlot()) {
            return;
        }
        event.setCancelled(false);
        final Inventory topInventory = event.getView().getTopInventory();
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
        if (this.apply(event, topInventory, shapedRecipe)) {
            return;
        }
        craftMatrix.setShaped(false);
        final Recipe shapelessMatrix = this.api.getCraftingSystem().getShapelessRecipe(craftMatrix);
        if (this.apply(event, topInventory, shapelessMatrix)) {
            return;
        }
        event.setCancelled(true);
        new CraftingScheduler(this.api, event.getView()).runTask(this.api.getPlugin());
    }

    private boolean apply(@NonNull InventoryClickEvent event, @NonNull Inventory topInventory, @Nullable Recipe recipe) {
        if (recipe != null) {
            final ItemStack[] itemStacks = this.collectItemStacks(topInventory);
            recipe.apply(itemStacks);
            this.applyItemStacks(topInventory, itemStacks);
            new CraftingScheduler(this.api, event.getView()).runTask(this.api.getPlugin());
            return true;
        }
        return false;
    }
}
