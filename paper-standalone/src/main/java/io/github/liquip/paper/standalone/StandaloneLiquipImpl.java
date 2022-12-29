package io.github.liquip.paper.standalone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.sqyyy.jcougar.JCougar;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPIConfig;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.NamespacedKeyArgument;
import io.github.liquip.api.Liquip;
import io.github.liquip.api.Registry;
import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import io.github.liquip.api.item.crafting.CraftingSystem;
import io.github.liquip.paper.core.item.enchantment.BukkitEnchantment;
import io.github.liquip.paper.core.item.feature.minecraft.AttributeModifierFeature;
import io.github.liquip.paper.core.item.feature.minecraft.CustomModelDataFeature;
import io.github.liquip.paper.core.item.feature.minecraft.HideAttributesFeature;
import io.github.liquip.paper.core.item.feature.minecraft.HideDyeFeature;
import io.github.liquip.paper.core.item.feature.minecraft.HideEnchantmentsFeature;
import io.github.liquip.paper.core.item.feature.minecraft.HidePotionEffectsFeature;
import io.github.liquip.paper.core.item.feature.minecraft.HideUnbreakableFeature;
import io.github.liquip.paper.core.item.feature.minecraft.LeatherDyeFeature;
import io.github.liquip.paper.core.item.feature.minecraft.UnbreakableFeature;
import io.github.liquip.paper.core.listener.BlockEventListener;
import io.github.liquip.paper.core.listener.EntityEventListener;
import io.github.liquip.paper.core.listener.PlayerEventListener;
import io.github.liquip.paper.core.listener.SystemEventListener;
import io.github.liquip.paper.core.util.RegistryImpl;
import io.github.liquip.paper.standalone.config.ConfigLoader;
import io.github.liquip.paper.standalone.item.crafting.CraftingSystemImpl;
import io.github.liquip.paper.standalone.item.crafting.CraftingUiManager;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class StandaloneLiquipImpl implements Liquip {
    public static final MiniMessage MM = MiniMessage.miniMessage();
    public static final String NAMESPACE = "liquip";
    private static final NamespacedKey PDC_KEY = new NamespacedKey(NAMESPACE, "key");
    private static final int COLOR_OK = 0x32cd32;
    private static final int COLOR_ERROR = 0xe32636;
    private final Plugin plugin;
    private final ObjectMapper mapper;
    private final ConfigLoader configLoader;
    private final CraftingUiManager craftingUiManager;
    private final Registry<Item> itemRegistry;
    private final Registry<Feature> featureRegistry;
    private final Registry<TaggedFeature<?>> taggedFeatureRegistry;
    private final Registry<Enchantment> enchantmentRegistry;
    private final CraftingSystem craftingSystem;
    private final Set<Key> configItems;
    private boolean currentlyLoadingConfig;
    private boolean loaded;
    private boolean enabled;

    public StandaloneLiquipImpl(@NonNull Plugin plugin) {
        this.plugin = plugin;
        this.mapper = new JsonMapper().enable(JsonParser.Feature.ALLOW_COMMENTS)
            .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
            .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.configLoader = new ConfigLoader(this);
        this.craftingUiManager = new CraftingUiManager(this);
        this.itemRegistry = new RegistryImpl<>();
        this.featureRegistry = new RegistryImpl<>();
        this.taggedFeatureRegistry = new RegistryImpl<>();
        this.enchantmentRegistry = new RegistryImpl<>();
        this.craftingSystem = new CraftingSystemImpl();
        this.configItems = new HashSet<>();
        this.currentlyLoadingConfig = false;
        this.loaded = false;
        this.enabled = false;
    }

    public static @NonNull Component MM(@NonNull String input) {
        return MM.deserialize(input);
    }

    void loadSystem() {
        if (this.loaded) {
            throw new IllegalStateException("System already loaded");
        }
        this.loaded = true;
        this.registerMinecraftFeatures();
        this.registerBukkitEnchantments();
        CommandAPI.onLoad(new CommandAPIConfig().silentLogs(true));
        final CommandAPICommand liquipCommand = new CommandAPICommand("liquip").withPermission("liquip.command");
        final CommandAPICommand liquipGiveCommand = new CommandAPICommand("give").withPermission("liquip.command.give")
            .withArguments(new NamespacedKeyArgument("key"))
            .executesPlayer(this::giveSubcommand);
        final CommandAPICommand liquipCraftCommand = new CommandAPICommand("craft").withPermission("liquip.command.craft")
            .executesPlayer((player, args) -> {
                this.craftingUiManager.openCraftingTable(player);
            });
        final CommandAPICommand liquipReloadCommand = new CommandAPICommand("reload").withPermission("liquip.command.reload")
            .executes(this::reloadSubcommand);
        final CommandAPICommand liquipDumpCommand = new CommandAPICommand("dump").withPermission("liquip.command.dump")
            .withArguments(new MultiLiteralArgument("items", "features", "tagged_features", "enchantments"))
            .executes(this::dumpSubcommand);
        liquipCommand.withSubcommands(liquipGiveCommand, liquipCraftCommand, liquipReloadCommand, liquipDumpCommand)
            .register();
    }

    void enableSystem() {
        if (this.enabled) {
            throw new IllegalStateException("System already enabled");
        }
        this.enabled = true;
        CommandAPI.onEnable(this.plugin);
        JCougar.initializeSystem(this.plugin);
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new SystemEventListener(this), this.plugin);
        pluginManager.registerEvents(new BlockEventListener(this), this.plugin);
        pluginManager.registerEvents(new EntityEventListener(this), this.plugin);
        pluginManager.registerEvents(new PlayerEventListener(this), this.plugin);
        this.currentlyLoadingConfig = true;
        if (!this.configLoader.loadConfig()) {
            this.plugin.getSLF4JLogger()
                .error("Could not load config, disabling...");
            Bukkit.getPluginManager()
                .disablePlugin(this.plugin);
            return;
        }
        this.currentlyLoadingConfig = false;
        this.plugin.getSLF4JLogger()
            .info("Successfully loaded config");
        this.craftingUiManager.loadCatalogue();
    }

    boolean reloadSystem() {
        for (final Key configItem : this.configItems) {
            this.itemRegistry.unregister(configItem);
        }
        this.configItems.clear();
        this.currentlyLoadingConfig = true;
        final boolean result = this.configLoader.loadConfig();
        this.currentlyLoadingConfig = false;
        this.craftingUiManager.loadCatalogue();
        return result;
    }

    void disableSystem() {
        CommandAPI.onDisable();
    }

    public void addConfigItem(@NonNull Item item) {
        if (!this.currentlyLoadingConfig) {
            throw new IllegalStateException("Not loading config currently");
        }
        this.itemRegistry.register(item.key(), item);
        this.configItems.add(item.key());
    }

    public @NonNull Plugin getPlugin() {
        return this.plugin;
    }

    public @NonNull ObjectMapper getMapper() {
        return this.mapper;
    }

    public @NonNull ConfigLoader getConfigLoader() {
        return this.configLoader;
    }

    public @NonNull CraftingUiManager getCraftingUiManager() {
        return this.craftingUiManager;
    }

    @Override
    public @NonNull Logger getSystemLogger() {
        return this.plugin.getSLF4JLogger();
    }

    @Override
    public @NonNull Registry<Item> getItemRegistry() {
        return this.itemRegistry;
    }

    @Override
    public @NonNull Registry<Feature> getFeatureRegistry() {
        return this.featureRegistry;
    }

    @Override
    public @NonNull Registry<TaggedFeature<?>> getTaggedFeatureRegistry() {
        return this.taggedFeatureRegistry;
    }

    @Override
    public @NonNull Registry<Enchantment> getEnchantmentRegistry() {
        return this.enchantmentRegistry;
    }

    @Override
    public boolean supportsCraftingSystem() {
        return true;
    }

    @Override
    public @NonNull CraftingSystem getCraftingSystem() {
        return this.craftingSystem;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public boolean isCustomItemStack(@NonNull ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) {
            return false;
        }
        final PersistentDataContainer persistentDataContainer = itemStack.getItemMeta()
            .getPersistentDataContainer();
        if (!persistentDataContainer.has(PDC_KEY, PersistentDataType.STRING)) {
            return false;
        }
        return NamespacedKey.fromString(persistentDataContainer.get(PDC_KEY, PersistentDataType.STRING)) != null;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public @NonNull Key getKeyFromItemStack(@NonNull ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) {
            return itemStack.getType()
                .getKey();
        }
        final PersistentDataContainer persistentDataContainer = itemStack.getItemMeta()
            .getPersistentDataContainer();
        if (!persistentDataContainer.has(PDC_KEY, PersistentDataType.STRING)) {
            return itemStack.getType()
                .getKey();
        }
        return Objects.requireNonNull(NamespacedKey.fromString(persistentDataContainer.get(PDC_KEY, PersistentDataType.STRING)));
    }

    @Override
    public void setKeyForItemStack(@NonNull ItemStack itemStack, @NonNull Key key) {
        itemStack.editMeta(meta -> meta.getPersistentDataContainer()
            .set(PDC_KEY, PersistentDataType.STRING, key.asString()));
    }

    private void registerMinecraftFeatures() {
        final HideAttributesFeature hideAttributesFeature = new HideAttributesFeature();
        this.featureRegistry.register(hideAttributesFeature.getKey(), hideAttributesFeature);
        final HideDyeFeature hideDyeFeature = new HideDyeFeature();
        this.featureRegistry.register(hideDyeFeature.getKey(), hideDyeFeature);
        final HideEnchantmentsFeature hideEnchantmentsFeature = new HideEnchantmentsFeature();
        this.featureRegistry.register(hideEnchantmentsFeature.getKey(), hideEnchantmentsFeature);
        final HidePotionEffectsFeature hidePotionEffectsFeature = new HidePotionEffectsFeature();
        this.featureRegistry.register(hidePotionEffectsFeature.getKey(), hidePotionEffectsFeature);
        final HideUnbreakableFeature hideUnbreakableFeature = new HideUnbreakableFeature();
        this.featureRegistry.register(hideUnbreakableFeature.getKey(), hideUnbreakableFeature);
        final UnbreakableFeature unbreakableFeature = new UnbreakableFeature();
        this.featureRegistry.register(unbreakableFeature.getKey(), unbreakableFeature);
        // Tagged features
        final AttributeModifierFeature attributeModifierFeature = new AttributeModifierFeature();
        this.taggedFeatureRegistry.register(attributeModifierFeature.getKey(), attributeModifierFeature);
        final CustomModelDataFeature customModelDataFeature = new CustomModelDataFeature();
        this.taggedFeatureRegistry.register(customModelDataFeature.getKey(), customModelDataFeature);
        final LeatherDyeFeature leatherDyeFeature = new LeatherDyeFeature();
        this.taggedFeatureRegistry.register(leatherDyeFeature.getKey(), leatherDyeFeature);
    }

    private void registerBukkitEnchantments() {
        for (org.bukkit.enchantments.Enchantment value : org.bukkit.enchantments.Enchantment.values()) {
            this.enchantmentRegistry.register(value.getKey(), new BukkitEnchantment(value));
        }
    }

    private void giveSubcommand(Player player, Object[] args) {
        final NamespacedKey key = (NamespacedKey) args[0];
        final Item item = this.itemRegistry.get(key);
        if (item == null) {
            player.sendMessage(Component.text("Item could not be found")
                .color(TextColor.color(COLOR_ERROR)));
            return;
        }
        player.getInventory()
            .addItem(item.newItemStack());
        player.sendMessage(Component.text("Gave [" + key.asString() + "] to " + player.getName())
            .color(TextColor.color(COLOR_OK)));
    }

    private void reloadSubcommand(CommandSender sender, Object[] args) {
        if (this.reloadSystem()) {
            sender.sendMessage(Component.text("Successfully reloaded config")
                .color(TextColor.color(COLOR_OK)));
        } else {
            sender.sendMessage(Component.text("Could not reload config")
                .color(TextColor.color(COLOR_ERROR)));
        }
    }

    private void dumpSubcommand(CommandSender sender, Object[] args) {
        switch ((String) args[0]) {
            case "items" -> {
                for (final Item item : this.itemRegistry) {
                    sender.sendMessage(Component.text(item.key()
                        .asString()));
                }
            }
            case "features" -> {
                for (final Feature feature : this.featureRegistry) {
                    sender.sendMessage(Component.text(feature.key()
                        .asString()));
                }
            }
            case "tagged_features" -> {
                for (final TaggedFeature<?> taggedFeature : this.taggedFeatureRegistry) {
                    sender.sendMessage(Component.text(taggedFeature.key()
                        .asString()));
                }
            }
            case "enchantments" -> {
                for (final Enchantment enchantment : this.enchantmentRegistry) {
                    sender.sendMessage(Component.text(enchantment.key()
                        .asString()));
                }
            }
        }
    }
}