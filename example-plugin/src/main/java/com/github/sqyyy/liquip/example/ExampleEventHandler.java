package com.github.sqyyy.liquip.example;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.LightningStrikeEvent;

public class ExampleEventHandler implements Listener {
    private final LiquipExample plugin;

    public ExampleEventHandler(LiquipExample plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getPlayerData().put(event.getPlayer().getUniqueId(), new PlayerData());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getPlayerData().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLightingStrike(EntityDamageByEntityEvent event) {
        if (event.getDamager().getScoreboardTags().contains("liquip:staff_of_power_lightning")) {
            if (event.getEntityType() == EntityType.DROPPED_ITEM) {
                event.setCancelled(true);
                return;
            }
            event.setDamage(event.getDamage() * 6);
        }
    }

    @EventHandler
    public void onLightingStrike(LightningStrikeEvent event) {
        for (LivingEntity entity : event.getLightning().getLocation().getNearbyLivingEntities(1)) {
            if (entity.getType() == EntityType.DROPPED_ITEM) {
                continue;
            }
            entity.damage(600);
        }
    }
}
