package io.github.liquip.paper.standalone.item.crafting;

import com.github.sqyyy.liquip.gui.Slot;
import com.github.sqyyy.liquip.gui.impl.ListenerPane;
import io.github.liquip.paper.standalone.StandaloneLiquipImpl;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CraftingPane extends ListenerPane {
    private final StandaloneLiquipImpl api;

    public CraftingPane(StandaloneLiquipImpl api, int priority) {
        super(priority, Slot.ROW_TWO_SLOT_TWO, Slot.ROW_FOUR_SLOT_FOUR);
        this.api = api;
    }

    @Override
    public void onPutItem(@NotNull InventoryClickEvent event) {
        new CraftingUpdateScheduler(event.getView()).runTaskLater(this.api.getPlugin(), 0);
    }

    @Override
    public void onTakeItem(@NotNull InventoryClickEvent event) {
        new CraftingUpdateScheduler(event.getView()).runTaskLater(this.api.getPlugin(), 0);
    }

    @Override
    public void onClickItem(@NotNull InventoryClickEvent event) {
    }

    @Override
    public void onDragItems(@NotNull InventoryDragEvent event) {
        new CraftingUpdateScheduler(event.getView()).runTaskLater(this.api.getPlugin(), 0);
    }

    @Override
    public void onCloseInventory(@NotNull InventoryCloseEvent event) {
        final Inventory topInventory = event.getView().getTopInventory();
        final Inventory bottomInventory = event.getView().getBottomInventory();
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                final int slot = (row + 1) * 9 + column + 1;
                final ItemStack item = topInventory.getItem(slot);
                if (item == null || item.getType() == Material.AIR) {
                    continue;
                }
                if (bottomInventory.firstEmpty() != -1) {
                    bottomInventory.addItem(item);
                    continue;
                }
                final HumanEntity player = event.getPlayer();
                player.getWorld().dropItem(player.getEyeLocation(), item);
            }
        }
    }
}
