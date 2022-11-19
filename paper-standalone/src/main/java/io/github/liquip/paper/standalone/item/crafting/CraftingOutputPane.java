package io.github.liquip.paper.standalone.item.crafting;

import com.github.sqyyy.liquip.gui.Slot;
import com.github.sqyyy.liquip.gui.impl.OutputSlotPane;
import io.github.liquip.paper.standalone.StandaloneLiquipImpl;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CraftingOutputPane extends OutputSlotPane {
    private final StandaloneLiquipImpl api;

    public CraftingOutputPane(StandaloneLiquipImpl api, int priority) {
        super(priority, Slot.ROW_THREE_SLOT_SEVEN);
        this.api = api;
    }

    private boolean craft(@NotNull InventoryClickEvent event, @NotNull ItemStack @NotNull [] items,
        @NotNull NamespacedKey @NotNull [] keys, @NotNull CraftingHashObject craftingHashObject,
        @NotNull CraftingRegistry registry) {
        final CraftingRecipe recipe = registry.get(craftingHashObject);
        if (!recipe.matches(items, keys)) {
            event.setCancelled(true);
            new CraftingUpdateScheduler(api, event.getView()).runTaskLater(api.getPlugin(), 0);
            return true;
        }
        recipe.craft(items, event);
        new CraftingUpdateScheduler(api, event.getView()).runTaskLater(api.getPlugin(), 0);
        return false;
    }

    @Override
    public void onTakeItem(@NotNull InventoryClickEvent event) {
        if (event.getRawSlot() != Slot.ROW_THREE_SLOT_SEVEN.getSlot()) {
            return;
        }
        final Inventory topInventory = event.getView().getTopInventory();
        final ItemStack[] items = new ItemStack[9];
        final NamespacedKey[] identifiers = new NamespacedKey[9];
        final ItemStack air = new ItemStack(Material.AIR);
        final NamespacedKey airId = new NamespacedKey("minecraft", "air");
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                final ItemStack item = topInventory.getItem((row + 1) * 9 + column + 1);
                items[row * 3 + column] = item != null ? item : air;
                identifiers[row * 3 + column] =
                    item != null ? api.getKeyFromItemStack(item) : airId;
            }
        }
        final CraftingHashObject craftingHashObject = new CraftingHashObject(items, true);
        final CraftingRegistry registry = api.getCraftingRegistry(); // TODO crafting registry
        if (registry.isRegistered(craftingHashObject)) {
            if (craft(event, items, identifiers, craftingHashObject, registry))
                return;
            return;
        }
        craftingHashObject.setShaped(false);
        if (!registry.isRegistered(craftingHashObject)) {
            event.setCancelled(true);
            new CraftingUpdateScheduler(api, event.getView()).runTaskLater(api.getPlugin(), 0);
            return;
        }
        if (craft(event, items, identifiers, craftingHashObject, registry))
            return;
    }
}
