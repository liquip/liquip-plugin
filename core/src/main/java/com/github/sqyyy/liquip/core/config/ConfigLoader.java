package com.github.sqyyy.liquip.core.config;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.LiquipProvider;
import com.github.sqyyy.liquip.core.features.MinecraftFeatures;
import com.github.sqyyy.liquip.core.items.LeveledEnchantment;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.core.util.Identifier;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ConfigLoader {
    private ComponentLogger componentLogger = null;
    private Config currentItemConfig = null;
    private List<String> itemPaths = null;
    private Identifier customCraftItem = new Identifier("liquip", "custom_crafting_table");
    private boolean customCraftItemRegistered = false;

    private static void onCraftItemInteract(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.HAND) {
            Liquip.getProvider().getCraftingMenu().open(event.getPlayer());
        }
    }

    public boolean loadConfig() {
        Plugin plugin = Liquip.getProvidingPlugin(Liquip.class);
        componentLogger = plugin.getComponentLogger();
        final Path pluginPath = Path.of(plugin.getDataFolder().toURI());
        if (!createDirectory(pluginPath, false)) {
            return false;
        }
        final Path itemsPath = pluginPath.resolve("items");
        if (!createDirectory(itemsPath, false)) {
            return false;
        }
        final Path configPath = pluginPath.resolve("config.conf");
        if (!createDefaultFile(configPath, """
            custom_craft_item = "liquip:custom_crafting_table"
            items = []
            """, false)) {
            return false;
        }
        if (!parseConfig(configPath, false)) {
            return false;
        }
        for (String itemPathString : itemPaths) {
            if (itemPathString.contains("..")) {
                log("Path to file for item <aqua>'{}'</aqua> cannot include '..'", true,
                    itemPathString);
                continue;
            }
            final Path itemPath = itemsPath.resolve(itemPathString);
            if (!checkItemFile(itemPath, true)) {
                continue;
            }
            loadItem(itemPath);
        }
        if (!customCraftItemRegistered) {
            final LiquipProvider provider = Liquip.getProvider();
            final LiquipItem.Builder craftingTable =
                new LiquipItem.Builder(customCraftItem, "<aqua>Custom Crafting Table</aqua>",
                    Material.CRAFTING_TABLE);
            craftingTable.enchantment(new LeveledEnchantment(Enchantment.DURABILITY, 1));
            craftingTable.feature(Objects.requireNonNull(
                provider.getFeatureRegistry().get(MinecraftFeatures.HIDE_ENCHANTMENTS)));
            craftingTable.event(PlayerInteractEvent.class, ConfigLoader::onCraftItemInteract);
            Liquip.getProvider().getItemRegistry()
                .register(customCraftItem, Objects.requireNonNull(craftingTable.build()));
        }
        return true;
    }

    private void loadItem(@NotNull Path path) {
        if (!parseItemConfig(path, true)) {
            return;
        }
        final Optional<LiquipItem> itemResult =
            LiquipItem.fromConfig(componentLogger, path, currentItemConfig, Liquip.getProvider());
        if (itemResult.isEmpty()) {
            return;
        }
        final LiquipItem item = itemResult.get();
        if (!customCraftItemRegistered && customCraftItem.equals(item.getId())) {
            item.registerEvent(PlayerInteractEvent.class, ConfigLoader::onCraftItemInteract);
            customCraftItemRegistered = true;
        }
        if (Liquip.getProvider().getItemRegistry().register(item.getId(), item)) {
            return;
        }
        log("Could not register item <aqua>'{}'</aqua>", true, item.getId());
    }

    private boolean createDirectory(Path path, boolean warnOnly) {
        if (Files.exists(path)) {
            if (Files.isDirectory(path)) {
                return true;
            }
            log("Could not create directory <aqua>'{}'</aqua> as it's a file", warnOnly,
                path.toAbsolutePath());
            return false;
        }
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            log("Could not create directory <aqua>'{}'</aqua>", warnOnly, path.toAbsolutePath());
            return false;
        }
        return true;
    }

    private boolean createDefaultFile(Path path, String contents, boolean warnOnly) {
        if (Files.exists(path)) {
            if (!Files.isDirectory(path)) {
                return true;
            }
            log("Could not create file <aqua>'{}'</aqua> as it's a directory", warnOnly,
                path.toAbsolutePath());
            return false;
        }
        try {
            Files.createFile(path);
            Files.writeString(path, contents);
        } catch (IOException e) {
            log("Could not create or write to file <aqua>'{}'</aqua>", warnOnly,
                path.toAbsolutePath());
            return false;
        }
        return true;
    }

    private boolean parseConfig(Path path, boolean warnOnly) {
        Config config;
        try {
            config = ConfigFactory.parseFile(path.toFile());
        } catch (ConfigException exception) {
            log("Could not parse file <aqua>'{}'</aqua>", warnOnly, path.toAbsolutePath());
            return false;
        }
        if (config == null) {
            log("Could not parse file <aqua>'{}'</aqua>", warnOnly, path.toAbsolutePath());
            return false;
        }
        if (!config.hasPath("items")) {
            log("Config has not items-tag. For more information read https://github" +
                ".com/sqyyy-jar/liquip/wiki/config.conf", warnOnly);
            return false;
        }
        try {
            itemPaths = config.getStringList("items");
        } catch (ConfigException.WrongType exception) {
            log("Wrong type for items in config. For more information read https://github" +
                ".com/sqyyy-jar/liquip/wiki/config.conf", warnOnly);
            return false;
        } catch (ConfigException exception) {
            log("Could not parse config. For more information read https://github" +
                ".com/sqyyy-jar/liquip/wiki/config.conf", warnOnly);
            return false;
        }
        if (itemPaths == null) {
            log("Could not parse config. For more information read https://github" +
                ".com/sqyyy-jar/liquip/wiki/config.conf", warnOnly);
            return false;
        }
        return true;
    }

    private boolean parseItemConfig(Path path, boolean warnOnly) {
        try {
            currentItemConfig = ConfigFactory.parseFile(path.toFile());
        } catch (ConfigException exception) {
            log("Could not parse file for item <aqua>'{}'</aqua>: {}", warnOnly,
                path.toAbsolutePath(), exception.getMessage());
            return false;
        }
        if (currentItemConfig == null) {
            log("Could not parse file for item <aqua>'{}'</aqua>", warnOnly, path.toAbsolutePath());
            return false;
        }
        if (!currentItemConfig.hasPath("id")) {
            log("Could not parse file for item <aqua>'{}'</aqua>", warnOnly, path.toAbsolutePath());
            log("  Config has no id-tag. For more information read https://github" +
                ".com/sqyyy-jar/liquip/wiki/Custom-items", warnOnly);
            return false;
        }
        if (!currentItemConfig.hasPath("material")) {
            log("Could not parse file for item <aqua>'{}'</aqua>", warnOnly, path.toAbsolutePath());
            log("  Config has no material-tag. For more information read https://github" +
                ".com/sqyyy-jar/liquip/wiki/Custom-items", warnOnly);
            return false;
        }
        if (!currentItemConfig.hasPath("name")) {
            log("Could not parse file for item <aqua>'{}'</aqua>", warnOnly, path.toAbsolutePath());
            log("  Config has no name-tag", warnOnly);
            log("  For more information read https://github.com/sqyyy-jar/liquip/wiki/Custom-items",
                warnOnly);
            return false;
        }
        if (!checkItemConfigString(path, "id", true) ||
            !checkItemConfigString(path, "material", true) ||
            !checkItemConfigString(path, "name", true)) {
            log("Could not parse file for item <aqua>'{}'</aqua>", warnOnly, path.toAbsolutePath());
            log("  Config has no valid id, material and name tags", warnOnly);
            log("  For more information read https://github.com/sqyyy-jar/liquip/wiki/Custom-items",
                warnOnly);
            return false;
        }
        return true;
    }

    private boolean checkItemConfigString(Path path, String tag, boolean warnOnly) {
        try {
            if (currentItemConfig.getString(tag) == null) {
                log("Could not parse file for item <aqua>'{}'</aqua>", warnOnly,
                    path.toAbsolutePath());
                log("  The tag '{}' is null", warnOnly, tag);
                log("  For more information read https://github" +
                    ".com/sqyyy-jar/liquip/wiki/Custom-items", warnOnly);
                return false;
            }
        } catch (ConfigException.WrongType exception) {
            log("Could not parse file for item <aqua>'{}'</aqua>", warnOnly, path.toAbsolutePath());
            log("  The tag '{}' has a wrong type", warnOnly, tag);
            log("  For more information read https://github.com/sqyyy-jar/liquip/wiki/Custom-items",
                warnOnly);
            return false;
        } catch (ConfigException exception) {
            log("Could not parse file for item <aqua>'{}'</aqua>", warnOnly, path.toAbsolutePath());
            log("  The tag '{}' is invalid", warnOnly, tag);
            log("  For more information read https://github.com/sqyyy-jar/liquip/wiki/Custom-items",
                warnOnly);
            return false;
        }
        return true;
    }

    private boolean checkItemFile(Path path, boolean warnOnly) {
        if (Files.exists(path)) {
            if (!Files.isDirectory(path)) {
                return true;
            }
            log("Path for item <aqua>'{}'</aqua> should not be a directory", warnOnly,
                path.toAbsolutePath());
            return false;
        }
        log("File for item <aqua>'{}'</aqua> does not exist", warnOnly, path.toAbsolutePath());
        return false;
    }

    private void log(String message, boolean warnOnly, Object... args) {
        if (warnOnly) {
            componentLogger.warn(MiniMessage.miniMessage().deserialize(message), args);
            return;
        }
        componentLogger.error(MiniMessage.miniMessage().deserialize(message), args);
    }
}
