package com.github.sqyyy.liquip;

import com.github.sqyyy.liquip.config.ConfigLoader;
import com.github.sqyyy.liquip.dev.DevCommand;
import com.github.sqyyy.liquip.event.BlockEventListener;
import com.github.sqyyy.liquip.event.PlayerEventListener;
import com.github.sqyyy.liquip.util.Warning;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Liquip extends JavaPlugin {
    public static final String DEFAULT_NAMESPACE = LiquipProvider.DEFAULT_NAMESPACE;
    private static final LiquipProvider provider = new LiquipProvider();

    public static LiquipProvider getProvider() {
        return provider;
    }

    @Override
    public void onEnable() {
        provider.registerDefaultFeatures(true);
        final var configLoader = new ConfigLoader(this);
        final var configResult = configLoader.loadConfig();

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

        Bukkit.getPluginManager().registerEvents(new BlockEventListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerEventListener(this), this);

        final var devCommand = getCommand("liquip");

        if (devCommand == null) {
            getSLF4JLogger().warn("Could not load liquip-command");
            return;
        }

        devCommand.setExecutor(new DevCommand(this));
    }
}
