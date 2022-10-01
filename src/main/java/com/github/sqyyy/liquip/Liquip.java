package com.github.sqyyy.liquip;

import com.github.sqyyy.liquip.config.ConfigLoader;
import com.github.sqyyy.liquip.dev.DevCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public class Liquip extends JavaPlugin {
    public static final String DEFAULT_NAMESPACE = "minecraft";
    private static final LiquipProvider provider = new LiquipProvider();
    private final Logger logger = getSLF4JLogger();

    public static LiquipProvider getProvider() {
        return provider;
    }

    @Override
    public void onEnable() {
        final var configLoader = new ConfigLoader(this);
        final var configResult = configLoader.loadConfig();

        if (configResult.isPresent()) {
            logger.error("An error occurred while loading config: {}", configResult.get().getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }

        final var devCommand = getCommand("liquip");

        if (devCommand == null) {
            return;
        }

        devCommand.setExecutor(new DevCommand(this));
    }
}
