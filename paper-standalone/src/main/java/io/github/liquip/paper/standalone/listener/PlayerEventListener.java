package io.github.liquip.paper.standalone.listener;

import io.github.liquip.api.item.Item;
import io.github.liquip.paper.standalone.StandaloneLiquipImpl;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class PlayerEventListener implements Listener {
    private final StandaloneLiquipImpl api;

    public PlayerEventListener(StandaloneLiquipImpl api) {
        this.api = api;
    }

    @EventHandler
    public void onInteract(@NotNull PlayerInteractEvent event) {
        final ItemStack hand;
        if (event.getHand() == EquipmentSlot.HAND) {
            hand = event.getPlayer().getInventory().getItemInMainHand();
        } else {
            hand = event.getPlayer().getInventory().getItemInOffHand();
        }
        this.handleItem(PlayerInteractEvent.class, event, hand);
    }

    @EventHandler
    public void onInteract(@NotNull PlayerInteractEntityEvent event) {
        final ItemStack hand;
        if (event.getHand() == EquipmentSlot.HAND) {
            hand = event.getPlayer().getInventory().getItemInMainHand();
        } else {
            hand = event.getPlayer().getInventory().getItemInOffHand();
        }
        this.handleItem(PlayerInteractEntityEvent.class, event, hand);
    }

    @EventHandler
    public void onInteract(@NotNull PlayerInteractAtEntityEvent event) {
        final ItemStack hand;
        if (event.getHand() == EquipmentSlot.HAND) {
            hand = event.getPlayer().getInventory().getItemInMainHand();
        } else {
            hand = event.getPlayer().getInventory().getItemInOffHand();
        }
        this.handleItem(PlayerInteractAtEntityEvent.class, event, hand);
    }

    @EventHandler
    public void onRod(@NotNull PlayerFishEvent event) {
        final PlayerInventory inventory = event.getPlayer().getInventory();
        final ItemStack mainHand = inventory.getItemInMainHand();
        final ItemStack offHand = inventory.getItemInOffHand();
        if (mainHand.getType() == Material.FISHING_ROD) {
            this.handleItem(PlayerFishEvent.class, event, mainHand);
        }
        if (offHand.getType() == Material.FISHING_ROD) {
            this.handleItem(PlayerFishEvent.class, event, offHand);
        }
    }

    @EventHandler
    public void onBucketEmpty(@NotNull PlayerBucketEmptyEvent event) {
        final ItemStack hand;
        if (event.getHand() == EquipmentSlot.HAND) {
            hand = event.getPlayer().getInventory().getItemInMainHand();
        } else {
            hand = event.getPlayer().getInventory().getItemInOffHand();
        }
        this.handleItem(PlayerBucketEmptyEvent.class, event, hand);
    }

    private <T extends PlayerEvent> void handleItem(@NotNull Class<T> eventClass, @NonNull T event, @NonNull ItemStack stack) {
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
