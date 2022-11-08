package io.github.liquip.api;

import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import org.bukkit.Registry;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Liquip {
    @NonNull Registry<Item> getItemRegistry();

    @NonNull Registry<Feature> getFeatureRegistry();

    @NonNull Registry<TaggedFeature<?>> getModifierRegistry();

    @NonNull Registry<Enchantment> getEnchantmentRegistry();
}
