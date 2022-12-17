package io.github.liquip.paper.core.listener;

import io.github.liquip.api.Liquip;
import io.github.liquip.api.item.Item;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EntityEventListener implements Listener {
    private final Liquip api;

    public EntityEventListener(@NonNull Liquip api) {
        this.api = api;
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }
        final PlayerInventory inventory = player.getInventory();
        this.handleItem(EntityDamageByEntityEvent.class, event, inventory.getItemInMainHand());
        this.handleItem(EntityDamageByEntityEvent.class, event, inventory.getItemInOffHand());
        this.handleItem(EntityDamageByEntityEvent.class, event, inventory.getHelmet());
        this.handleItem(EntityDamageByEntityEvent.class, event, inventory.getChestplate());
        this.handleItem(EntityDamageByEntityEvent.class, event, inventory.getLeggings());
        this.handleItem(EntityDamageByEntityEvent.class, event, inventory.getBoots());
    }

    private <T extends EntityEvent> void handleItem(@NonNull Class<T> eventClass, @NonNull T event, @Nullable ItemStack stack) {
        if (stack == null) {
            return;
        }
        if (!this.api.isCustomItemStack(stack)) {
            return;
        }
        final Key identifierResult = this.api.getKeyFromItemStack(stack);
        final Item item = this.api.getItemRegistry().get(identifierResult);
        if (item != null) {
            item.callEvent(eventClass, event, stack);
        }
    }
}
