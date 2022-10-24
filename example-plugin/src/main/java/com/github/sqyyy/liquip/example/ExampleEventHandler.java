package com.github.sqyyy.liquip.example;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
}
