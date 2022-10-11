package com.github.sqyyy.liquip.core;

import com.github.sqyyy.liquip.core.config.ConfigLoader;
import com.github.sqyyy.liquip.core.dev.DevCommand;
import com.github.sqyyy.liquip.core.event.BlockEventListener;
import com.github.sqyyy.liquip.core.event.PlayerEventListener;
import com.github.sqyyy.liquip.core.util.Status;
import com.github.sqyyy.liquip.core.util.Warning;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Liquip extends JavaPlugin {
    public static final String DEFAULT_NAMESPACE = LiquipProvider.DEFAULT_NAMESPACE;
    private static final LiquipProvider provider = new LiquipProvider();

    public static LiquipProvider getProvider() {
        return provider;
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIConfig());
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable(this);
        provider.registerDefaults();
        final ConfigLoader configLoader = new ConfigLoader();
        final Status<Void> configResult = configLoader.loadConfig();
        for (Warning warning : configResult.getWarnings()) {
            warning.print(getSLF4JLogger());
        }
        if (configResult.isErr()) {
            configResult.unwrapErr().print(getSLF4JLogger());
            getSLF4JLogger().error("Could not load config successfully");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        } else {
            getSLF4JLogger().info("Successfully loaded config");
        }
        Bukkit.getPluginManager().registerEvents(new BlockEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerEventListener(), this);
        new DevCommand();
    }
}
