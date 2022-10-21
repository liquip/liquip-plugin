package com.github.sqyyy.liquip.example.features;

import com.github.sqyyy.liquip.core.items.Feature;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class AspectOfTheEnd implements Feature {
    @Override
    public void initialize(@NotNull LiquipItem item) {
        item.registerEvent(PlayerInteractEvent.class, this::onInteract);
    }

    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }
        final Player player = event.getPlayer();
        final Vector vec = player.getEyeLocation().getDirection().multiply(8);
        final Location pos = player.getLocation().add(vec);
        final Material lowerType = pos.getBlock().getType();
        if (lowerType != Material.AIR && lowerType != Material.CAVE_AIR && lowerType != Material.VOID_AIR) {
            player.sendMessage(Component.text("Your sight is obstructed").color(TextColor.color(0xFF0000)));
            return;
        }
        final Material upperType = pos.add(0, 1, 0).getBlock().getType();
        if (upperType != Material.AIR && upperType != Material.CAVE_AIR && upperType != Material.VOID_AIR) {
            player.sendMessage(Component.text("Your sight is obstructed").color(TextColor.color(0xFF0000)));
            return;
        }
        player.teleport(pos);
        player.setFallDistance(Math.max(player.getFallDistance(), 8));
    }
}
