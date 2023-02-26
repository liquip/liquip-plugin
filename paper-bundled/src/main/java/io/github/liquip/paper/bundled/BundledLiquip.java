package io.github.liquip.paper.bundled;

import io.github.liquip.api.Liquip;
import io.github.liquip.api.Registry;
import io.github.liquip.api.event.EventSystem;
import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import io.github.liquip.paper.core.event.HashEventSystem;
import io.github.liquip.paper.core.listener.BlockEventListener;
import io.github.liquip.paper.core.listener.EntityEventListener;
import io.github.liquip.paper.core.listener.PlayerEventListener;
import io.github.liquip.paper.core.listener.SystemEventListener;
import io.github.liquip.paper.core.util.HashRegistry;
import io.github.liquip.paper.core.util.api.ApiRegistrationUtil;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Objects;

public class BundledLiquip implements Liquip {
    protected final NamespacedKey pdcKey;
    protected final Plugin plugin;
    protected final Logger systemLogger;
    protected final Registry<Item> itemRegistry;
    protected final Registry<Feature> featureRegistry;
    protected final Registry<TaggedFeature<?>> taggedFeatureRegistry;
    protected final Registry<Enchantment> enchantmentRegistry;
    protected final EventSystem eventSystem;
    protected boolean enabled;

    public BundledLiquip(@NotNull String namespace, @NotNull Plugin plugin, boolean register) {
        this(namespace, plugin, plugin.getSLF4JLogger(), register);
    }

    public BundledLiquip(@NotNull String namespace, @NotNull Plugin plugin, @NotNull Logger systemLogger, boolean register) {
        Objects.requireNonNull(namespace);
        Objects.requireNonNull(plugin);
        Objects.requireNonNull(systemLogger);
        this.pdcKey = new NamespacedKey(namespace.toLowerCase(), "key");
        this.plugin = plugin;
        this.systemLogger = systemLogger;
        this.itemRegistry = new HashRegistry<>();
        this.featureRegistry = new HashRegistry<>();
        this.taggedFeatureRegistry = new HashRegistry<>();
        this.enchantmentRegistry = new HashRegistry<>();
        this.eventSystem = new HashEventSystem();
        this.enabled = false;
        if (register) {
            ApiRegistrationUtil.registerProvider(this);
        }
    }

    public void enableSystem() {
        if (this.enabled) {
            throw new IllegalStateException("System already enabled");
        }
        this.enabled = true;
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new SystemEventListener(this), this.plugin);
        pluginManager.registerEvents(new BlockEventListener(this), this.plugin);
        pluginManager.registerEvents(new EntityEventListener(this), this.plugin);
        pluginManager.registerEvents(new PlayerEventListener(this), this.plugin);
    }

    @Override
    public @NotNull Logger getSystemLogger() {
        return this.systemLogger;
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
        return false;
    }

    @Override
    public boolean supportsEventSystem() {
        return true;
    }

    @Override
    public @NotNull EventSystem getEventSystem() {
        return Liquip.super.getEventSystem();
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public boolean isCustomItemStack(@NotNull ItemStack itemStack) {
        Objects.requireNonNull(itemStack);
        if (itemStack.getItemMeta() == null) {
            return false;
        }
        final PersistentDataContainer persistentDataContainer = itemStack.getItemMeta()
            .getPersistentDataContainer();
        if (!persistentDataContainer.has(this.pdcKey, PersistentDataType.STRING)) {
            return false;
        }
        return NamespacedKey.fromString(persistentDataContainer.get(this.pdcKey, PersistentDataType.STRING)) != null;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public @NotNull Key getKeyFromItemStack(@NotNull ItemStack itemStack) {
        Objects.requireNonNull(itemStack);
        if (itemStack.getItemMeta() == null) {
            return itemStack.getType()
                .getKey();
        }
        final PersistentDataContainer persistentDataContainer = itemStack.getItemMeta()
            .getPersistentDataContainer();
        if (!persistentDataContainer.has(this.pdcKey, PersistentDataType.STRING)) {
            return itemStack.getType()
                .getKey();
        }
        return Objects.requireNonNull(
            NamespacedKey.fromString(persistentDataContainer.get(this.pdcKey, PersistentDataType.STRING)));
    }

    @Override
    public void setKeyForItemStack(@NotNull ItemStack itemStack, @NotNull Key key) {
        Objects.requireNonNull(itemStack);
        Objects.requireNonNull(key);
        itemStack.editMeta(meta -> meta.getPersistentDataContainer()
            .set(this.pdcKey, PersistentDataType.STRING, key.asString()));
    }
}
