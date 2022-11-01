package com.github.sqyyy.liquip.core;

import com.github.sqyyy.liquip.core.dev.DevCommand;
import com.github.sqyyy.liquip.core.event.BlockEventListener;
import com.github.sqyyy.liquip.core.event.PlayerEventListener;
import com.github.sqyyy.liquip.core.event.SystemEventListener;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Liquip extends JavaPlugin {
    private static final LiquipProvider provider = new LiquipProvider();

    public static LiquipProvider getProvider() {
        return provider;
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIConfig());
        provider.registerDefaults();
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable(this);
        provider.load();
        Bukkit.getPluginManager().registerEvents(new BlockEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new SystemEventListener(), this);
        new DevCommand();
    }
}
