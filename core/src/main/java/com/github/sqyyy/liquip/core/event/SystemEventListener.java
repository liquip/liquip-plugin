package com.github.sqyyy.liquip.core.event;

import com.github.sqyyy.liquip.core.items.LiquipItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class SystemEventListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraft(CraftItemEvent event) {
        for (ItemStack itemStack : event.getInventory()) {
            if (itemStack == null) {
                continue;
            }
            if (LiquipItem.hasCustomIdentifier(itemStack)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        final ItemStack itemStack = event.getItemInHand();
        if (LiquipItem.hasCustomIdentifier(itemStack)) {
            event.setCancelled(true);
        }
    }
}
