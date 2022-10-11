package com.github.sqyyy.liquip.core;

import com.github.sqyyy.liquip.core.features.MinecraftFeatures;
import com.github.sqyyy.liquip.core.items.Feature;
import com.github.sqyyy.liquip.core.items.LiquipEnchantment;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.core.system.craft.CraftingRegistry;
import com.github.sqyyy.liquip.core.util.Identifier;
import com.github.sqyyy.liquip.core.util.Registry;
import com.github.sqyyy.liquip.core.util.impl.BasicRegistry;
import org.jetbrains.annotations.NotNull;

public class LiquipProvider {
    public static final String DEFAULT_NAMESPACE = "minecraft";
    private final Registry<Feature> features;
    private final Registry<LiquipItem> items;
    private final Registry<LiquipEnchantment> enchantments;
    private final CraftingRegistry recipes;

    public LiquipProvider() {
        features = new BasicRegistry<>();
        items = new BasicRegistry<>();
        enchantments = new BasicRegistry<>();
        recipes = new CraftingRegistry();
    }

    public @NotNull Registry<@NotNull Feature> getFeatureRegistry() {
        return features;
    }

    public @NotNull Registry<@NotNull LiquipItem> getItemRegistry() {
        return items;
    }

    public @NotNull Registry<@NotNull LiquipEnchantment> getEnchantmentRegistry() {
        return enchantments;
    }

    public @NotNull CraftingRegistry getCraftingRegistry() {
        return recipes;
    }

    public void registerDefaultFeatures() {
        features.register(new Identifier(DEFAULT_NAMESPACE, "unbreakable"), new MinecraftFeatures.Unbreakable());
        features.register(new Identifier(DEFAULT_NAMESPACE, "hide_enchantments"),
                new MinecraftFeatures.HideEnchantments());
        features.register(new Identifier(DEFAULT_NAMESPACE, "hide_unbreakable"),
                new MinecraftFeatures.HideUnbreakable());
    }
}
