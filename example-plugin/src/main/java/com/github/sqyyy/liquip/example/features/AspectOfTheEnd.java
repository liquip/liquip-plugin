package com.github.sqyyy.liquip.example.features;

import com.github.sqyyy.liquip.core.items.Feature;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.example.LiquipExample;
import com.github.sqyyy.liquip.example.PlayerData;
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
    private final LiquipExample plugin;

    public AspectOfTheEnd(LiquipExample plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize(@NotNull LiquipItem item) {
        item.registerEvent(PlayerInteractEvent.class, this::onInteract);
    }

    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        final Player player = event.getPlayer();
        final PlayerData playerData = plugin.getPlayerData().get(player.getUniqueId());
        if (playerData == null) {
            throw new IllegalStateException("PlayerData for player '" + player.getName() + "' (" + player.getUniqueId() +
                    ") not found. This may be caused by a reload!");
        }
        if (playerData.isAspectOfTheEndCooldown()) {
            player.sendMessage(Component.text("Item is on cooldown").color(TextColor.color(0xDD1C1A)));
            return;
        }
        final Vector vec = player.getEyeLocation().getDirection().multiply(8);
        final Location pos = player.getLocation().add(vec);
        final Material lowerType = pos.getBlock().getType();
        if (lowerType != Material.AIR && lowerType != Material.CAVE_AIR && lowerType != Material.VOID_AIR) {
            player.sendMessage(Component.text("Your sight is obstructed").color(TextColor.color(0xDD1C1A)));
            return;
        }
        final Material upperType = pos.add(0, 1, 0).getBlock().getType();
        if (upperType != Material.AIR && upperType != Material.CAVE_AIR && upperType != Material.VOID_AIR) {
            player.sendMessage(Component.text("Your sight is obstructed").color(TextColor.color(0xDD1C1A)));
            return;
        }
        player.teleport(pos);
        player.setFallDistance(Math.min(player.getFallDistance(), 8));
        playerData.cooldownAspectOftTheEnd(50 * 5);
    }
}
