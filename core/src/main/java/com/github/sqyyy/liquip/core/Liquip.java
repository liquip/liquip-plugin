package com.github.sqyyy.liquip.core;

import com.github.sqyyy.liquip.core.dev.DevCommand;
import com.github.sqyyy.liquip.core.event.BlockEventListener;
import com.github.sqyyy.liquip.core.event.PlayerEventListener;
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
        provider.load();
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable(this);
        Bukkit.getPluginManager().registerEvents(new BlockEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerEventListener(), this);
        new DevCommand();
    }
}
