package com.github.sqyyy.liquip.core.config;

import com.github.sqyyy.liquip.core.system.LiquipError;
import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.core.system.IgnoredError;
import com.github.sqyyy.liquip.core.util.Status;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ConfigLoader {
    private final Liquip liquip;

    public ConfigLoader(Liquip liquip) {
        this.liquip = liquip;
    }

    public Status<Void> loadConfig() {
        final var status = new Status<Void>();
        final var pluginPath = Path.of(liquip.getDataFolder().toURI());

        if (!Files.exists(pluginPath)) {
            try {
                Files.createDirectory(pluginPath);
            } catch (IOException e) {
                status.setError(LiquipError.CONFIG_IO_EXCEPTION);
                return status;
            }
        }
        if (!Files.isDirectory(pluginPath)) {
            status.setError(LiquipError.PLUGIN_DIRECTORY_DOES_NOT_EXIST);
            return status;
        }

        final var configPath = pluginPath.resolve("config.hocon");

        if (!Files.exists(configPath)) {
            try {
                Files.createFile(configPath);
                Files.writeString(configPath, """
                        items: [
                        ]
                        """);
            } catch (IOException e) {
                status.setError(LiquipError.CONFIG_IO_EXCEPTION);
                return status;
            }
        }
        if (Files.isDirectory(configPath)) {
            status.setError(LiquipError.CONFIG_IS_DIRECTORY);
            return status;
        }

        Config config;

        try {
            config = ConfigFactory.parseFile(configPath.toFile());
        } catch (ConfigException exception) {
            status.setError(LiquipError.INVALID_CONFIG);
            return status;
        }
        if (config == null) {
            status.setError(LiquipError.NULL_CONFIG);
            return status;
        }
        if (!config.hasPath("items")) {
            status.setError(LiquipError.NO_ITEMS_REGISTRY);
            return status;
        }

        final List<String> itemsRegistry;

        try {
            itemsRegistry = config.getStringList("items");
        } catch (ConfigException.WrongType exception) {
            status.setError(LiquipError.ITEMS_REGISTRY_WRONG_TYPE);
            return status;
        }
        if (itemsRegistry == null) {
            status.setError(LiquipError.NULL_CONFIG);
            return status;
        }

        for (String itemPath : itemsRegistry) {
            final var item = loadItem(itemPath);
            status.addWarnings(item.getWarnings());

            if (item.isErr()) {
                status.addWarning(new IgnoredError("Could not load item in '" + itemPath + "'", item.unwrapErr()));
            }
        }

        status.setOk(true);
        status.setValue(null);
        return status;
    }

    public Status<Void> loadItem(String path) {
        final var status = new Status<Void>();
        final var itemsPath = Path.of(liquip.getDataFolder().toURI()).resolve("items");

        if (!Files.exists(itemsPath)) {
            try {
                Files.createDirectory(itemsPath);
            } catch (IOException e) {
                status.setError(LiquipError.CONFIG_IO_EXCEPTION);
                return status;
            }
        }
        if (!Files.isDirectory(itemsPath)) {
            status.setError(LiquipError.ITEMS_DIRECTORY_IS_FILE);
            return status;
        }
        if (path.contains("..")) {
            status.setError(LiquipError.ITEM_PATH_INVALID);
            return status;
        }

        final var itemPath = itemsPath.resolve(Paths.get(path));

        if (!Files.exists(itemPath)) {
            status.setError(LiquipError.ITEM_FILE_NOT_FOUND);
            return status;
        }
        if (Files.isDirectory(itemPath)) {
            status.setError(LiquipError.ITEM_FILE_IS_DIRECTORY);
            return status;
        }

        final var itemConfig = ConfigFactory.parseFile(itemPath.toFile());
        final var itemResult = LiquipItem.fromConfig(itemConfig, Liquip.getProvider().getEnchantmentRegistry(),
                Liquip.getProvider().getFeatureRegistry(), Liquip.getProvider().getCraftingRegistry());
        status.addWarnings(itemResult.getWarnings());

        if (itemResult.isErr()) {
            status.setError(LiquipError.ITEM_INVALID);
            return status;
        }

        final var item = itemResult.unwrap();

        if (!Liquip.getProvider().getItemRegistry().register(item.getId(), item)) {
            status.setError(LiquipError.COULD_NOT_REGISTER);
            return status;
        }

        Bukkit.broadcast(Component.text("registered new item " + item.getId()));
        status.setOk(true);
        status.setValue(null);
        return status;
    }
}
