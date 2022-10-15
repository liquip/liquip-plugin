package com.github.sqyyy.liquip.core;

import com.github.sqyyy.liquip.core.config.ConfigLoader;
import com.github.sqyyy.liquip.core.features.MinecraftFeatures;
import com.github.sqyyy.liquip.core.items.Feature;
import com.github.sqyyy.liquip.core.items.LiquipEnchantment;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.core.items.ModifierSupplier;
import com.github.sqyyy.liquip.core.items.impl.AttributeModifierModifierSupplier;
import com.github.sqyyy.liquip.core.system.craft.CraftingRegistry;
import com.github.sqyyy.liquip.core.util.Identifier;
import com.github.sqyyy.liquip.core.util.Registry;
import com.github.sqyyy.liquip.core.util.impl.BasicRegistry;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class LiquipProvider {
    public static final String DEFAULT_NAMESPACE = "minecraft";
    private final Registry<Feature> features;
    private final Registry<ModifierSupplier> modifiers;
    private final Registry<LiquipEnchantment> enchantments;
    private final ConfigLoader configLoader;
    private Registry<LiquipItem> items;
    private CraftingRegistry recipes;

    public LiquipProvider() {
        items = new BasicRegistry<>();
        features = new BasicRegistry<>();
        modifiers = new BasicRegistry<>();
        enchantments = new BasicRegistry<>();
        recipes = new CraftingRegistry();
        configLoader = new ConfigLoader();
    }

    public @NotNull Registry<@NotNull LiquipItem> getItemRegistry() {
        return items;
    }

    public @NotNull Registry<@NotNull Feature> getFeatureRegistry() {
        return features;
    }

    public Registry<ModifierSupplier> getModifierRegistry() {
        return modifiers;
    }

    public @NotNull Registry<@NotNull LiquipEnchantment> getEnchantmentRegistry() {
        return enchantments;
    }

    public @NotNull CraftingRegistry getCraftingRegistry() {
        return recipes;
    }

    public void registerDefaults() {
        features.register(new Identifier(DEFAULT_NAMESPACE, "unbreakable"), new MinecraftFeatures.Unbreakable());
        features.register(new Identifier(DEFAULT_NAMESPACE, "hide_enchantments"),
                new MinecraftFeatures.HideEnchantments());
        features.register(new Identifier(DEFAULT_NAMESPACE, "hide_unbreakable"),
                new MinecraftFeatures.HideUnbreakable());
        modifiers.register(new Identifier(DEFAULT_NAMESPACE, "attribute_modifier"),
                new AttributeModifierModifierSupplier());
    }

    void load() {
        if (configLoader.loadConfig()) {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger().info("Successfully loaded config");
        } else {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger().error("Could not load config successfully");
            Bukkit.getPluginManager().disablePlugin(Liquip.getProvidingPlugin(Liquip.class));
        }
    }

    public boolean reload() {
        items = new BasicRegistry<>();
        recipes = new CraftingRegistry();
        if (configLoader.loadConfig()) {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger().info("Successfully reloaded config");
            return true;
        }
        Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger().error("Could not reload config successfully");
        Bukkit.getPluginManager().disablePlugin(Liquip.getProvidingPlugin(Liquip.class));
        return false;
    }
}
