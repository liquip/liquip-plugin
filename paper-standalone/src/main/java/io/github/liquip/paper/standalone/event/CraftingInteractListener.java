package io.github.liquip.paper.standalone.event;

import io.github.liquip.paper.standalone.StandaloneLiquip;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CraftingInteractListener implements Listener {
    private static final NamespacedKey KEY = new NamespacedKey("liquip", "crafting_table");
    private final StandaloneLiquip api;

    public CraftingInteractListener(@NotNull StandaloneLiquip api) {
        Objects.requireNonNull(api);
        this.api = api;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = player.getInventory()
            .getItemInMainHand();
        if (!api.isCustomItemStack(item)) {
            return;
        }
        if (!KEY.equals(api.getKeyFromItemStack(item))) {
            return;
        }
        if (event.getPlayer()
            .getOpenInventory()
            .getTopInventory()
            .getType() != InventoryType.CRAFTING) {
            return;
        }
        event.setCancelled(true);
        this.api.getCraftingUiManager()
            .openCraftingTable(event.getPlayer());
    }
}
