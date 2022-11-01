package com.github.sqyyy.liquip.core.event;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.core.util.Identifier;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlayerEventListener implements Listener {
    @EventHandler
    public void onInteract(@NotNull PlayerInteractEvent event) {
        final ItemStack hand;
        if (event.getHand() == EquipmentSlot.HAND) {
            hand = event.getPlayer().getInventory().getItemInMainHand();
        } else {
            hand = event.getPlayer().getInventory().getItemInOffHand();
        }
        handleItem(PlayerInteractEvent.class, event, hand);
    }

    @EventHandler
    public void onInteract(@NotNull PlayerInteractEntityEvent event) {
        final ItemStack hand;
        if (event.getHand() == EquipmentSlot.HAND) {
            hand = event.getPlayer().getInventory().getItemInMainHand();
        } else {
            hand = event.getPlayer().getInventory().getItemInOffHand();
        }
        handleItem(PlayerInteractEntityEvent.class, event, hand);
    }

    @EventHandler
    public void onInteract(@NotNull PlayerInteractAtEntityEvent event) {
        final ItemStack hand;
        if (event.getHand() == EquipmentSlot.HAND) {
            hand = event.getPlayer().getInventory().getItemInMainHand();
        } else {
            hand = event.getPlayer().getInventory().getItemInOffHand();
        }
        handleItem(PlayerInteractAtEntityEvent.class, event, hand);
    }

    @EventHandler
    public void onRod(@NotNull PlayerFishEvent event) {
        final PlayerInventory inventory = event.getPlayer().getInventory();
        final ItemStack mainHand = inventory.getItemInMainHand();
        final ItemStack offHand = inventory.getItemInOffHand();
        if (mainHand.getType() == Material.FISHING_ROD) {
            handleItem(PlayerFishEvent.class, event, mainHand);
        }
        if (offHand.getType() == Material.FISHING_ROD) {
            handleItem(PlayerFishEvent.class, event, offHand);
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
        handleItem(PlayerBucketEmptyEvent.class, event, hand);
    }

    private <T extends PlayerEvent> void handleItem(Class<T> eventClass, T event, ItemStack itemStack) {
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
