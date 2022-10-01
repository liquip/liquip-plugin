package com.github.sqyyy.liquip;

import com.github.sqyyy.liquip.config.ConfigLoader;
import com.github.sqyyy.liquip.dev.DevCommand;
import com.github.sqyyy.liquip.items.Feature;
import com.github.sqyyy.liquip.items.LiquipEnchantment;
import com.github.sqyyy.liquip.items.LiquipItem;
import com.github.sqyyy.liquip.util.BasicRegistry;
import com.github.sqyyy.liquip.util.Registry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class Liquip extends JavaPlugin {
    public static final String DEFAULT_NAMESPACE = "minecraft";
    private final Registry<Feature> features = new BasicRegistry<>();
    private final Registry<LiquipItem> items = new BasicRegistry<>();
    private final Registry<LiquipEnchantment> enchantments = new BasicRegistry<>();
    private final Logger logger = getSLF4JLogger();

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

    public Registry<Feature> getFeatureRegistry() {
        return features;
    }

    public Registry<LiquipItem> getItemRegistry() {
        return items;
    }

    public Registry<LiquipEnchantment> getEnchantmentRegistry() {
        return enchantments;
    }

    @NotNull
    public Logger getPublicLogger() {
        return logger;
    }
}
