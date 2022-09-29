package com.github.sqyyy.liquip;

import com.github.sqyyy.liquip.config.ConfigLoader;
import com.github.sqyyy.liquip.items.Feature;
import com.github.sqyyy.liquip.items.LiquipItem;
import com.github.sqyyy.liquip.util.Registry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class Liquip extends JavaPlugin {
    public static final String DEFAULT_NAMESPACE = "minecraft";
    private final Registry<Feature> features = new Registry.BasicRegistry<>();
    private final Registry<LiquipItem> items = new Registry.BasicRegistry<>();
    private final Logger logger = getSLF4JLogger();

    @Override
    public void onEnable() {
        final var configLoader = new ConfigLoader(this);
        final var configResult = configLoader.loadConfig();

        if (configResult.isPresent()) {
            logger.error("An error occurred while loading config: {}", configResult.get().getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public Registry<Feature> getFeatures() {
        return features;
    }

    public Registry<LiquipItem> getItems() {
        return items;
    }

    @NotNull
    public Logger getPublicLogger() {
        return logger;
    }
}
