package io.github.liquip.paper.bundled;

import io.github.liquip.api.Liquip;
import io.github.liquip.api.Registry;
import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import io.github.liquip.api.item.crafting.CraftingSystem;
import io.github.liquip.paper.core.listener.BlockEventListener;
import io.github.liquip.paper.core.listener.EntityEventListener;
import io.github.liquip.paper.core.listener.PlayerEventListener;
import io.github.liquip.paper.core.listener.SystemEventListener;
import io.github.liquip.paper.core.util.RegistryImpl;
import io.github.liquip.paper.core.util.api.ApiRegistrationUtil;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

public class BundledLiquipImpl implements Liquip {
    private final NamespacedKey pdcKey;
    private final Plugin plugin;
    private final Registry<Item> itemRegistry;
    private final Registry<Feature> featureRegistry;
    private final Registry<TaggedFeature<?>> taggedFeatureRegistry;
    private final Registry<Enchantment> enchantmentRegistry;

    public BundledLiquipImpl(@NonNull String namespace, @NonNull Plugin plugin, boolean register) {
        this.pdcKey = new NamespacedKey(namespace.toLowerCase(), "key");
        this.plugin = plugin;
        this.itemRegistry = new RegistryImpl<>();
        this.featureRegistry = new RegistryImpl<>();
        this.taggedFeatureRegistry = new RegistryImpl<>();
        this.enchantmentRegistry = new RegistryImpl<>();
        if (register) {
            ApiRegistrationUtil.registerProvider(this);
        }
    }

    public void enableSystem() {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new SystemEventListener(this), this.plugin);
        pluginManager.registerEvents(new BlockEventListener(this), this.plugin);
        pluginManager.registerEvents(new EntityEventListener(this), this.plugin);
        pluginManager.registerEvents(new PlayerEventListener(this), this.plugin);
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
        return false;
    }

    @Override
    public @NonNull CraftingSystem getCraftingSystem() {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public boolean isCustomItemStack(@NonNull ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) {
            return false;
        }
        final PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        if (!persistentDataContainer.has(this.pdcKey, PersistentDataType.STRING)) {
            return false;
        }
        return NamespacedKey.fromString(persistentDataContainer.get(this.pdcKey, PersistentDataType.STRING)) != null;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public @NonNull Key getKeyFromItemStack(@NonNull ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) {
            return itemStack.getType().getKey();
        }
        final PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        if (!persistentDataContainer.has(this.pdcKey, PersistentDataType.STRING)) {
            return itemStack.getType().getKey();
        }
        return Objects.requireNonNull(
            NamespacedKey.fromString(persistentDataContainer.get(this.pdcKey, PersistentDataType.STRING)));
    }

    @Override
    public void setKeyForItemStack(@NonNull ItemStack itemStack, @NonNull Key key) {
        itemStack.editMeta(meta -> meta.getPersistentDataContainer().set(this.pdcKey, PersistentDataType.STRING, key.asString()));
    }
}
