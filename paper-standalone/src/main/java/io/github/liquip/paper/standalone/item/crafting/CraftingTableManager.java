package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.item.crafting.Recipe;
import io.github.liquip.paper.standalone.StandaloneLiquip;
import net.kyori.adventure.key.KeyedValue;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CraftingTableManager {
    private final StandaloneLiquip api;

    public CraftingTableManager(@NotNull StandaloneLiquip api) {
        this.api = api;
    }

    private @Nullable ItemStack @NotNull [] collectItemStacks(@NotNull Inventory inventory) {
        return new ItemStack[]{inventory.getItem(10), inventory.getItem(11), inventory.getItem(12), inventory.getItem(
            19), inventory.getItem(20), inventory.getItem(21), inventory.getItem(28), inventory.getItem(
            29), inventory.getItem(30)};
    }

    private void applyItemStacks(@NotNull Inventory inventory, @NotNull ItemStack @NotNull [] stacks) {
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

    public boolean onTakeItem(@NotNull Player player, @NotNull InventoryView view, int slot) {
        final Inventory topInventory = view.getTopInventory();
        final List<KeyedValue<Integer>> stacks = new ArrayList<>(Collections.nCopies(9, null));
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                final ItemStack item = topInventory.getItem((row + 1) * 9 + column + 1);
                if (item == null || item.getType() == Material.AIR) {
                    stacks.set(row * 3 + column, null);
                    continue;
                }
                stacks.set(row * 3 + column,
                    KeyedValue.keyedValue(this.api.getKeyFromItemStack(item), item.getAmount()));
            }
        }
        final UnboundCraftMatrix craftMatrix = new UnboundCraftMatrix(true, stacks);
        final Recipe shapedRecipe = this.api.getCraftingSystem().getShapedRecipe(craftMatrix);
        if (this.apply(view, shapedRecipe)) {
            return false;
        }
        craftMatrix.setShaped(false);
        final Recipe shapelessMatrix = this.api.getCraftingSystem().getShapelessRecipe(craftMatrix);
        if (this.apply(view, shapelessMatrix)) {
            return false;
        }
        new CraftingScheduler(this.api, view).runTask(this.api.getPlugin());
        return true;
    }

    private boolean apply(@NotNull InventoryView view, @Nullable Recipe recipe) {
        if (recipe != null) {
            final ItemStack[] itemStacks = this.collectItemStacks(view.getTopInventory());
            recipe.apply(itemStacks);
            this.applyItemStacks(view.getTopInventory(), itemStacks);
            new CraftingScheduler(this.api, view).runTask(this.api.getPlugin());
            return true;
        }
        return false;
    }
}
