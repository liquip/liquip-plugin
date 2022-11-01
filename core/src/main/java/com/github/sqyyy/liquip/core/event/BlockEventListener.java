package com.github.sqyyy.liquip.core.event;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.core.util.Identifier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BlockEventListener implements Listener {
    @EventHandler
    public void onBreak(@NotNull BlockBreakEvent event) {
        final ItemStack mainHand = event.getPlayer().getInventory().getItemInMainHand();
        final ItemStack offHand = event.getPlayer().getInventory().getItemInOffHand();
        handleItem(BlockBreakEvent.class, event, mainHand);
        handleItem(BlockBreakEvent.class, event, offHand);
    }

    @EventHandler
    public void onPlace(@NotNull BlockPlaceEvent event) {
        final ItemStack hand;
        if (event.getHand() == EquipmentSlot.HAND) {
            hand = event.getPlayer().getInventory().getItemInMainHand();
        } else {
            hand = event.getPlayer().getInventory().getItemInOffHand();
        }
        handleItem(BlockPlaceEvent.class, event, hand);
    }

    private <T extends BlockEvent> void handleItem(Class<T> eventClass, T event, ItemStack itemStack) {
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
