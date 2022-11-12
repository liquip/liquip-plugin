package io.github.liquip.paper.standalone;

import io.github.liquip.api.Liquip;
import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import io.github.liquip.common.Registry;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class StandaloneLiquipImpl implements Liquip {
    public static final String NAMESPACE = "liquip";
    private static final NamespacedKey PDC_KEY = new NamespacedKey(NAMESPACE, "key");
    private final Plugin plugin;
    private final Registry<Item> itemRegistry;
    private final Registry<Feature> featureRegistry;
    private final Registry<TaggedFeature<?>> taggedFeatureRegistry;
    private final Registry<Enchantment> enchantmentRegistry;

    public StandaloneLiquipImpl(Plugin plugin) {
        this.plugin = plugin;
        this.itemRegistry = new Registry<>();
        this.featureRegistry = new Registry<>();
        this.taggedFeatureRegistry = new Registry<>();
        this.enchantmentRegistry = new Registry<>();
    }

    @Override
    public @NonNull Registry<Item> getItemRegistry() {
        return itemRegistry;
    }

    @Override
    public @NonNull Registry<Feature> getFeatureRegistry() {
        return featureRegistry;
    }

    @Override
    public @NonNull Registry<TaggedFeature<?>> getTaggedFeatureRegistry() {
        return taggedFeatureRegistry;
    }

    @Override
    public @NonNull Registry<Enchantment> getEnchantmentRegistry() {
        return enchantmentRegistry;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public boolean isCustomItemStack(@NonNull ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) {
            return false;
        }
        final PersistentDataContainer persistentDataContainer =
            itemStack.getItemMeta().getPersistentDataContainer();
        if (!persistentDataContainer.has(PDC_KEY, PersistentDataType.STRING)) {
            return false;
        }
        return NamespacedKey.fromString(
            persistentDataContainer.get(PDC_KEY, PersistentDataType.STRING)) != null;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public @NonNull NamespacedKey getKeyFromItemStack(@NonNull ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) {
            return itemStack.getType().getKey();
        }
        final PersistentDataContainer persistentDataContainer =
            itemStack.getItemMeta().getPersistentDataContainer();
        if (!persistentDataContainer.has(PDC_KEY, PersistentDataType.STRING)) {
            return itemStack.getType().getKey();
        }
        final NamespacedKey namespacedKey = NamespacedKey.fromString(
            persistentDataContainer.get(PDC_KEY, PersistentDataType.STRING));
        if (namespacedKey == null) {
            return itemStack.getType().getKey();
        }
        return namespacedKey;
    }

    @Override
    public void setKeyForItemStack(@NonNull ItemStack itemStack, @NonNull NamespacedKey key) {
        itemStack.editMeta(meta -> meta.getPersistentDataContainer()
            .set(PDC_KEY, PersistentDataType.STRING, key.asString()));
    }
}