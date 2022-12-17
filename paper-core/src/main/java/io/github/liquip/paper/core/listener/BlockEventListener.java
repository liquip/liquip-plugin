package io.github.liquip.paper.core.listener;

import io.github.liquip.api.Liquip;
import io.github.liquip.api.item.Item;
import net.kyori.adventure.key.Key;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class BlockEventListener implements Listener {
    private final Liquip api;

    public BlockEventListener(@NonNull Liquip api) {
        this.api = api;
    }

    @EventHandler
    public void onBreak(@NotNull BlockBreakEvent event) {
        final ItemStack mainHand = event.getPlayer().getInventory().getItemInMainHand();
        final ItemStack offHand = event.getPlayer().getInventory().getItemInOffHand();
        this.handleItem(BlockBreakEvent.class, event, mainHand);
        this.handleItem(BlockBreakEvent.class, event, offHand);
    }

    @EventHandler
    public void onPlace(@NotNull BlockPlaceEvent event) {
        final ItemStack hand;
        if (event.getHand() == EquipmentSlot.HAND) {
            hand = event.getPlayer().getInventory().getItemInMainHand();
        } else {
            hand = event.getPlayer().getInventory().getItemInOffHand();
        }
        this.handleItem(BlockPlaceEvent.class, event, hand);
    }

    private <T extends BlockEvent> void handleItem(@NonNull Class<T> eventClass, @NonNull T event, @NonNull ItemStack stack) {
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
