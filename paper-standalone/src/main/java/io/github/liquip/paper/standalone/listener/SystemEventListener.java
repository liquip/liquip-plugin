package io.github.liquip.paper.standalone.listener;

import io.github.liquip.paper.standalone.StandaloneLiquipImpl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class SystemEventListener implements Listener {
    private final StandaloneLiquipImpl api;

    public SystemEventListener(StandaloneLiquipImpl api) {
        this.api = api;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraft(CraftItemEvent event) {
        for (final ItemStack item : event.getInventory()) {
            if (item == null) {
                continue;
            }
            if (this.api.isCustomItemStack(item)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        final ItemStack item = event.getItemInHand();
        if (this.api.isCustomItemStack(item)) {
            event.setCancelled(true);
        }
    }
}
