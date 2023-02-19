package io.github.liquip.paper.core.listener;

import io.github.liquip.api.Liquip;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SystemEventListener implements Listener {
    private final Liquip api;

    public SystemEventListener(@NotNull Liquip api) {
        Objects.requireNonNull(api);
        this.api = api;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraft(CraftItemEvent event) {
        for (final ItemStack item : event.getInventory()
            .getMatrix()) {
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
