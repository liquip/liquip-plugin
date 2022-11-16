package com.github.sqyyy.liquip.core.event;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.core.util.Identifier;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Optional;

public class EntityEventListener implements Listener {
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }
        final PlayerInventory inventory = player.getInventory();
        handleItem(EntityDamageByEntityEvent.class, event, inventory.getItemInMainHand());
        handleItem(EntityDamageByEntityEvent.class, event, inventory.getItemInOffHand());
        handleItem(EntityDamageByEntityEvent.class, event, inventory.getHelmet());
        handleItem(EntityDamageByEntityEvent.class, event, inventory.getChestplate());
        handleItem(EntityDamageByEntityEvent.class, event, inventory.getLeggings());
        handleItem(EntityDamageByEntityEvent.class, event, inventory.getBoots());
    }

    private <T extends EntityEvent> void handleItem(Class<T> eventClass, T event, ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        if (!LiquipItem.hasCustomIdentifier(itemStack)) {
            return;
        }
        final Optional<Identifier> identifierResult = LiquipItem.getCustomIdentifier(itemStack);
        if (identifierResult.isEmpty()) {
            return;
        }
        final LiquipItem liquipItem = Liquip.getProvider().getItemRegistry().get(identifierResult.get());
        if (liquipItem != null) {
            liquipItem.callEvent(eventClass, event);
        }
    }
}
