package io.github.liquip.paper.core.item;

import io.github.liquip.api.Liquip;
import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Basic implementation of the {@link Item} interface.
 * <p>
 * This class is meant to be inherited and not used directly.
 * </p>
 */
public class ItemBase implements Item {
    protected final Liquip api;
    protected final NamespacedKey key;
    protected final Material material;
    protected final Component displayName;
    protected final List<Component> lore;
    protected final Object2IntMap<Enchantment> enchantments;
    protected final List<Feature> features;
    protected final Map<TaggedFeature<?>, Object> taggedFeatures;

    public ItemBase(@NotNull Liquip api, @NotNull NamespacedKey key, @NotNull Material material, @NotNull Component displayName) {
        this(api, key, material, displayName, List.of());
    }

    public ItemBase(@NotNull Liquip api, @NotNull NamespacedKey key, @NotNull Material material, @NotNull Component displayName,
        @NotNull List<Component> lore) {
        Objects.requireNonNull(api);
        Objects.requireNonNull(key);
        Objects.requireNonNull(material);
        Objects.requireNonNull(displayName);
        Objects.requireNonNull(lore);
        this.api = api;
        this.key = key;
        this.material = material;
        this.displayName = displayName;
        this.lore = new ArrayList<>(lore.size());
        this.lore.addAll(lore);
        this.enchantments = new Object2IntOpenHashMap<>();
        this.features = new ArrayList<>(0);
        this.taggedFeatures = new HashMap<>(0);
    }

    @Override
    public @NotNull ItemStack newItemStack() {
        final ItemStack itemStack = new ItemStack(material);
        itemStack.editMeta(meta -> {
            meta.displayName(displayName);
            meta.lore(lore);
        });
        enchantments.forEach((enchantment, level) -> enchantment.apply(this, itemStack, level));
        for (final Feature feature : features) {
            feature.apply(this, itemStack);
        }
        taggedFeatures.forEach((feature, obj) -> applyToTaggedFeature(feature, itemStack, obj));
        api.setKeyForItemStack(itemStack, key);
        return itemStack;
    }

    @Override
    public @NotNull NamespacedKey key() {
        return key;
    }

    @SuppressWarnings("unchecked")
    private <T> void applyToTaggedFeature(TaggedFeature<T> key, ItemStack itemStack, Object value) {
        key.apply(this, itemStack, (T) value);
    }
}
