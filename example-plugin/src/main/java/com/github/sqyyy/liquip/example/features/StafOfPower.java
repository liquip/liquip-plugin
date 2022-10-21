package com.github.sqyyy.liquip.example.features;

import com.github.sqyyy.liquip.core.items.Feature;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class StafOfPower implements Feature {
    @Override
    public void initialize(LiquipItem item) {
        item.registerEvent(PlayerInteractEvent.class, this::onInteract);
    }

    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR || event.getHand() != EquipmentSlot.HAND) {
            event.setCancelled(true);
            return;
        }
        final Player player = event.getPlayer();
    }
}
