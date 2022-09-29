package com.github.sqyyy.liquip.config;

import com.github.sqyyy.liquip.Liquip;
import com.github.sqyyy.liquip.items.LiquipItem;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class ConfigLoader {
    private final Liquip liquip;

    public ConfigLoader(Liquip liquip) {
        this.liquip = liquip;
    }

    public Optional<ConfigError> loadConfig() {
        final var pluginPath = Path.of(liquip.getDataFolder().toURI());

        if (!Files.exists(pluginPath)) {
            try {
                Files.createDirectory(pluginPath);
            } catch (IOException e) {
                return Optional.of(ConfigError.CONFIG_IO_EXCEPTION);
            }
        }
        if (!Files.isDirectory(pluginPath)) {
            return Optional.of(ConfigError.PLUGIN_DIRECTORY_DOES_NOT_EXIST);
        }

        final var configPath = pluginPath.resolve("config.hocon");

        if (!Files.exists(configPath)) {
            try {
                Files.createFile(configPath);
                Files.writeString(configPath,
                        """
                                items: [
                                ]
                                """
                );
            } catch (IOException e) {
                return Optional.of(ConfigError.CONFIG_IO_EXCEPTION);
            }
        }
        if (Files.isDirectory(configPath)) {
            return Optional.of(ConfigError.CONFIG_IS_DIRECTORY);
        }

        final var config = ConfigFactory.parseFile(configPath.toFile());

        if (config == null) {
            return Optional.of(ConfigError.NULL_CONFIG);
        }
        if (!config.hasPath("items")) {
            liquip.getPublicLogger().info("config {}", configPath);
            return Optional.of(ConfigError.NO_ITEMS_REGISTRY);
        }

        final List<String> itemsRegistry;

        try {
            itemsRegistry = config.getStringList("items");
        } catch (ConfigException.WrongType exception) {
            return Optional.of(ConfigError.ITEMS_REGISTRY_WRONG_TYPE);
        }
        if (itemsRegistry == null) {
            return Optional.of(ConfigError.NULL_CONFIG);
        }

        for (String itemPath : itemsRegistry) {
            final var item = loadItem(itemPath);

            if (item.isPresent()) {
                liquip.getPublicLogger()
                        .error("An error occurred while loading items: {} (in {})", item.get().getMessage(), itemPath);
            }
        }

        return Optional.empty();
    }

    public Optional<ConfigError> loadItem(String path) {
        final var itemsPath = Path.of(liquip.getDataFolder().toURI()).resolve("items");

        if (!Files.exists(itemsPath)) {
            try {
                Files.createDirectory(itemsPath);
            } catch (IOException e) {
                return Optional.of(ConfigError.CONFIG_IO_EXCEPTION);
            }
        }
        if (!Files.isDirectory(itemsPath)) {
            return Optional.of(ConfigError.ITEMS_DIRECTORY_IS_FILE);
        }

        // TODO check for ..
        final var itemPath = itemsPath.resolve(path);

        if (!Files.exists(itemPath)) {
            return Optional.of(ConfigError.ITEM_FILE_NOT_FOUND);
        }
        if (Files.isDirectory(itemPath)) {
            return Optional.of(ConfigError.ITEM_FILE_IS_DIRECTORY);
        }

        final var itemConfig = ConfigFactory.parseFile(itemPath.toFile());
        final var itemResult = LiquipItem.fromConfig(itemConfig);

        if (itemResult.isErr()) {
            return Optional.of(ConfigError.ITEM_INVALID);
        }

        final var item = itemResult.unwrap();
        liquip.getItems().register(item.getKey(), item);
        return Optional.empty();
    }
}
