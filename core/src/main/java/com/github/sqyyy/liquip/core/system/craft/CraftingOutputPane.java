package com.github.sqyyy.liquip.core.system.craft;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.core.util.Identifier;
import com.github.sqyyy.liquip.gui.Slot;
import com.github.sqyyy.liquip.gui.impl.OutputSlotPane;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CraftingOutputPane extends OutputSlotPane {
    public CraftingOutputPane(int priority) {
        super(priority, Slot.ROW_THREE_SLOT_SEVEN);
    }

    private boolean craft(@NotNull InventoryClickEvent event, @NotNull ItemStack @NotNull [] items,
                          @NotNull Identifier @NotNull [] identifiers, @NotNull CraftingHashObject craftingHashObject,
                          @NotNull CraftingRegistry registry) {
        final CraftingRecipe recipe = registry.get(craftingHashObject);
        if (!recipe.matches(items, identifiers)) {
            event.setCancelled(true);
            new CraftingUpdateScheduler(event.getView()).runTaskLater(Liquip.getProvidingPlugin(Liquip.class), 0);
            return true;
        }
        recipe.craft(items, event);
        new CraftingUpdateScheduler(event.getView()).runTaskLater(Liquip.getProvidingPlugin(Liquip.class), 0);
        return false;
    }

    @Override
    public void onTakeItem(@NotNull InventoryClickEvent event) {
        if (event.getRawSlot() != Slot.ROW_THREE_SLOT_SEVEN.getSlot()) {
            return;
        }
        final Inventory topInventory = event.getView().getTopInventory();
        final ItemStack[] items = new ItemStack[9];
        final Identifier[] identifiers = new Identifier[9];
        final ItemStack air = new ItemStack(Material.AIR);
        final Identifier airId = new Identifier("minecraft", "air");
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                final ItemStack item = topInventory.getItem((row + 1) * 9 + column + 1);
                items[row * 3 + column] = item != null ? item : air;
                identifiers[row * 3 + column] = item != null ? LiquipItem.getIdentifier(item) : airId;
            }
        }
        final CraftingHashObject craftingHashObject = new CraftingHashObject(items, true);
        final CraftingRegistry registry = Liquip.getProvider().getCraftingRegistry();
        if (registry.isRegistered(craftingHashObject)) {
            if (craft(event, items, identifiers, craftingHashObject, registry)) return;
            return;
        }
        craftingHashObject.setShaped(false);
        if (!registry.isRegistered(craftingHashObject)) {
            event.setCancelled(true);
            new CraftingUpdateScheduler(event.getView()).runTaskLater(Liquip.getProvidingPlugin(Liquip.class), 0);
            return;
        }
        if (craft(event, items, identifiers, craftingHashObject, registry)) return;
    }
}
