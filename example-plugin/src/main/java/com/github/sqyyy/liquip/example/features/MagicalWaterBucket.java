package com.github.sqyyy.liquip.example.features;

import com.github.sqyyy.liquip.core.items.Feature;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.jetbrains.annotations.NotNull;

public class MagicalWaterBucket implements Feature {
    @Override
    public void initialize(@NotNull LiquipItem item) {
        item.registerEvent(PlayerBucketEmptyEvent.class, this::onEmpty);
    }

    public void onEmpty(PlayerBucketEmptyEvent event) {
        event.setCancelled(true);
        final Block block = event.getBlock();
        if (block.getType() == Material.AIR) {
            event.getBlock().setType(Material.WATER);
            return;
        }
        if (block.getBlockData() instanceof Waterlogged waterlogged) {
            waterlogged.setWaterlogged(true);
        }
    }
}
