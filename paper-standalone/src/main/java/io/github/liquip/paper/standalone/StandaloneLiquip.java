package io.github.liquip.paper.standalone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.github.liquip.api.Liquip;
import io.github.liquip.api.Registry;
import io.github.liquip.api.event.EventSystem;
import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import io.github.liquip.api.item.crafting.CraftingSystem;
import io.github.liquip.api.item.crafting.Recipe;
import io.github.liquip.api.item.crafting.ShapedRecipe;
import io.github.liquip.api.item.crafting.ShapelessRecipe;
import io.github.liquip.paper.core.event.HashEventSystem;
import io.github.liquip.paper.core.item.feature.minecraft.AttributeModifierFeature;
import io.github.liquip.paper.core.item.feature.minecraft.CustomModelDataFeature;
import io.github.liquip.paper.core.item.feature.minecraft.HideAttributesFeature;
import io.github.liquip.paper.core.item.feature.minecraft.HideDyeFeature;
import io.github.liquip.paper.core.item.feature.minecraft.HideEnchantmentsFeature;
import io.github.liquip.paper.core.item.feature.minecraft.HideAdditionalTooltipFeature;
import io.github.liquip.paper.core.item.feature.minecraft.HideUnbreakableFeature;
import io.github.liquip.paper.core.item.feature.minecraft.LeatherDyeFeature;
import io.github.liquip.paper.core.item.feature.minecraft.SkullTextureFeature;
import io.github.liquip.paper.core.item.feature.minecraft.UnbreakableFeature;
import io.github.liquip.paper.core.listener.SystemEventListener;
import io.github.liquip.paper.core.util.HashRegistry;
import io.github.liquip.paper.standalone.command.CommandManager;
import io.github.liquip.paper.standalone.config.ConfigLoader;
import io.github.liquip.paper.standalone.item.crafting.CraftingUiManager;
import io.github.liquip.paper.standalone.item.crafting.StandaloneCraftingSystem;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class StandaloneLiquip implements Liquip {
    public static final MiniMessage MM = MiniMessage.miniMessage();
    public static final String NAMESPACE = "liquip";
    public static final TextColor COLOR_OK = TextColor.color(0x6BCD51);
    public static final TextColor COLOR_INFO = TextColor.color(0x57CDB9);
    public static final TextColor COLOR_ERROR = TextColor.color(0xE36B5D);
    private static final NamespacedKey PDC_KEY = new NamespacedKey(NAMESPACE, "key");
    private final JavaPlugin plugin;
    private final ObjectMapper mapper;
    private final ConfigLoader configLoader;
    private final CraftingUiManager craftingUiManager;
    private final CommandManager commandManager;
    private final Registry<Item> itemRegistry;
    private final Registry<Feature> featureRegistry;
    private final Registry<TaggedFeature<?>> taggedFeatureRegistry;
    private final Registry<Enchantment> enchantmentRegistry;
    private final CraftingSystem craftingSystem;
    private final EventSystem eventSystem;
    private final List<Service> services;
    private final Set<Key> configItems;
    private final Set<ShapedRecipe> configShapedRecipes;
    private final Set<ShapelessRecipe> configShapelessRecipes;
    private boolean currentlyLoadingConfig;
    private boolean loaded;
    private boolean enabled;

    public StandaloneLiquip(@NotNull JavaPlugin plugin) {
        Objects.requireNonNull(plugin);
        this.plugin = plugin;
        this.mapper = new JsonMapper()
            .enable(JsonParser.Feature.ALLOW_COMMENTS)
            .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
            .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.configLoader = new ConfigLoader(this);
        this.craftingUiManager = new CraftingUiManager(this);
        this.commandManager = new CommandManager(this);
        this.itemRegistry = new HashRegistry<>();
        this.featureRegistry = new HashRegistry<>();
        this.taggedFeatureRegistry = new HashRegistry<>();
        this.enchantmentRegistry = new HashRegistry<>();
        this.craftingSystem = new StandaloneCraftingSystem();
        this.eventSystem = new HashEventSystem();
        this.services = List.of(this.craftingUiManager, this.commandManager);
        this.configItems = new HashSet<>();
        this.configShapedRecipes = new HashSet<>();
        this.configShapelessRecipes = new HashSet<>();
        this.currentlyLoadingConfig = false;
        this.loaded = false;
        this.enabled = false;
    }

    public static @NotNull Component MM(@NotNull String input) {
        Objects.requireNonNull(input);
        return MM.deserialize(input);
    }

    void loadSystem() {
        if (this.loaded) {
            throw new IllegalStateException("System already loaded");
        }
        this.loaded = true;
        this.registerMinecraftFeatures();
        for (final Service service : this.services) {
            service.onLoad(this.plugin);
        }
    }

    void enableSystem() {
        if (this.enabled) {
            throw new IllegalStateException("System already enabled");
        }
        this.enabled = true;
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new SystemEventListener(this), this.plugin);
        this.currentlyLoadingConfig = true;
        if (!this.configLoader.loadConfig()) {
            this.plugin.getSLF4JLogger().error("Could not load config, disabling...");
            Bukkit.getPluginManager().disablePlugin(this.plugin);
            return;
        }
        this.currentlyLoadingConfig = false;
        this.plugin.getSLF4JLogger().info("Successfully loaded config");
        for (final Service service : this.services) {
            service.onEnable(this.plugin);
        }
    }

    public boolean reloadSystem() {
        for (final Key configItem : this.configItems) {
            this.itemRegistry.unregister(configItem);
        }
        this.configItems.clear();
        for (final ShapedRecipe configShapedRecipe : this.configShapedRecipes) {
            this.craftingSystem.unregisterShapedRecipe(configShapedRecipe);
        }
        this.configShapedRecipes.clear();
        for (final ShapelessRecipe configShapelessRecipe : this.configShapelessRecipes) {
            this.craftingSystem.unregisterShapelessRecipe(configShapelessRecipe);
        }
        this.configShapelessRecipes.clear();
        this.currentlyLoadingConfig = true;
        final boolean result = this.configLoader.loadConfig();
        this.currentlyLoadingConfig = false;
        for (final Service service : this.services) {
            service.onReload(this.plugin);
        }
        return result;
    }

    void disableSystem() {
        for (final Service service : this.services) {
            service.onDisable(this.plugin);
        }
    }

    public void addConfigItem(@NotNull Item item) {
        Objects.requireNonNull(item);
        if (!this.currentlyLoadingConfig) {
            throw new IllegalStateException("Not loading config currently");
        }
        this.itemRegistry.register(item.key(), item);
        this.configItems.add(item.key());
    }

    public void addConfigRecipe(@NotNull Recipe recipe) {
        Objects.requireNonNull(recipe);
        if (!this.currentlyLoadingConfig) {
            throw new IllegalStateException("Not loading config currently");
        }
        if (recipe instanceof ShapedRecipe shapedRecipe) {
            this.craftingSystem.registerShapedRecipe(shapedRecipe);
            this.configShapedRecipes.add(shapedRecipe);
            return;
        }
        if (recipe instanceof ShapelessRecipe shapelessRecipe) {
            this.craftingSystem.registerShapelessRecipe(shapelessRecipe);
            this.configShapelessRecipes.add(shapelessRecipe);
        }
    }

    public @NotNull JavaPlugin getPlugin() {
        return this.plugin;
    }

    public @NotNull ObjectMapper getMapper() {
        return this.mapper;
    }

    public @NotNull ConfigLoader getConfigLoader() {
        return this.configLoader;
    }

    public @NotNull CraftingUiManager getCraftingUiManager() {
        return this.craftingUiManager;
    }

    public @NotNull CommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public @NotNull Logger getSystemLogger() {
        return this.plugin.getSLF4JLogger();
    }

    @Override
    public @NotNull Registry<Item> getItemRegistry() {
        return this.itemRegistry;
    }

    @Override
    public @NotNull Registry<Feature> getFeatureRegistry() {
        return this.featureRegistry;
    }

    @Override
    public @NotNull Registry<TaggedFeature<?>> getTaggedFeatureRegistry() {
        return this.taggedFeatureRegistry;
    }

    @Override
    public @NotNull Registry<Enchantment> getEnchantmentRegistry() {
        return this.enchantmentRegistry;
    }

    @Override
    public boolean supportsCraftingSystem() {
        return true;
    }

    @Override
    public @NotNull CraftingSystem getCraftingSystem() {
        return this.craftingSystem;
    }

    @Override
    public boolean supportsEventSystem() {
        return true;
    }

    @Override
    public @NotNull EventSystem getEventSystem() {
        return this.eventSystem;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public boolean isCustomItemStack(@NotNull ItemStack itemStack) {
        Objects.requireNonNull(itemStack);
        if (itemStack.getItemMeta() == null) {
            return false;
        }
        final PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        if (!persistentDataContainer.has(PDC_KEY, PersistentDataType.STRING)) {
            return false;
        }
        return NamespacedKey.fromString(persistentDataContainer.get(PDC_KEY, PersistentDataType.STRING)) != null;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public @NotNull Key getKeyFromItemStack(@NotNull ItemStack itemStack) {
        Objects.requireNonNull(itemStack);
        if (itemStack.getItemMeta() == null) {
            return itemStack.getType().getKey();
        }
        final PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        if (!persistentDataContainer.has(PDC_KEY, PersistentDataType.STRING)) {
            return itemStack.getType().getKey();
        }
        return Objects.requireNonNull(
            NamespacedKey.fromString(persistentDataContainer.get(PDC_KEY, PersistentDataType.STRING)));
    }

    @Override
    public void setKeyForItemStack(@NotNull ItemStack itemStack, @NotNull Key key) {
        Objects.requireNonNull(itemStack);
        Objects.requireNonNull(key);
        itemStack.editMeta(
            meta -> meta.getPersistentDataContainer().set(PDC_KEY, PersistentDataType.STRING, key.asString()));
    }

    private void registerMinecraftFeatures() {
        final HideAttributesFeature hideAttributesFeature = new HideAttributesFeature();
        this.featureRegistry.register(hideAttributesFeature.getKey(), hideAttributesFeature);
        final HideDyeFeature hideDyeFeature = new HideDyeFeature();
        this.featureRegistry.register(hideDyeFeature.getKey(), hideDyeFeature);
        final HideEnchantmentsFeature hideEnchantmentsFeature = new HideEnchantmentsFeature();
        this.featureRegistry.register(hideEnchantmentsFeature.getKey(), hideEnchantmentsFeature);
        final HideAdditionalTooltipFeature hideAdditionalTooltipFeature = new HideAdditionalTooltipFeature();
        this.featureRegistry.register(hideAdditionalTooltipFeature.getKey(), hideAdditionalTooltipFeature);
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
        final SkullTextureFeature skullTextureFeature = new SkullTextureFeature();
        this.taggedFeatureRegistry.register(skullTextureFeature.getKey(), skullTextureFeature);
    }
}