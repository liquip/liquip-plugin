package com.github.sqyyy.liquip;

import com.github.sqyyy.liquip.features.MinecraftFeatures;
import com.github.sqyyy.liquip.features.custom.GrapplingHook;
import com.github.sqyyy.liquip.items.Feature;
import com.github.sqyyy.liquip.items.LiquipEnchantment;
import com.github.sqyyy.liquip.items.LiquipItem;
import com.github.sqyyy.liquip.util.impl.BasicRegistry;
import com.github.sqyyy.liquip.util.Identifier;
import com.github.sqyyy.liquip.util.Registry;

public class LiquipProvider {
    public static final String DEFAULT_NAMESPACE = "minecraft";
    private final Registry<Feature> features;
    private final Registry<LiquipItem> items;
    private final Registry<LiquipEnchantment> enchantments;

    public LiquipProvider() {
        features = new BasicRegistry<>();
        items = new BasicRegistry<>();
        enchantments = new BasicRegistry<>();
    }

    public Registry<Feature> getFeatureRegistry() {
        return features;
    }

    public Registry<LiquipItem> getItemRegistry() {
        return items;
    }

    public Registry<LiquipEnchantment> getEnchantmentRegistry() {
        return enchantments;
    }

    public void registerDefaultFeatures(boolean customFeatures) {
        features.register(new Identifier(DEFAULT_NAMESPACE, "unbreakable"), new MinecraftFeatures.Unbreakable());
        features.register(new Identifier(DEFAULT_NAMESPACE, "hide_enchantments"),
                new MinecraftFeatures.HideEnchantments());
        features.register(new Identifier(DEFAULT_NAMESPACE, "hide_unbreakable"),
                new MinecraftFeatures.HideUnbreakable());

        if (!customFeatures) {
            return;
        }

        features.register(new Identifier("liquip", "grappling_hook"), new GrapplingHook());
    }
}
