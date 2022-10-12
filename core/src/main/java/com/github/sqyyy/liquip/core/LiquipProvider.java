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
import com.github.sqyyy.liquip.core.util.Status;
import com.github.sqyyy.liquip.core.util.Warning;
import com.github.sqyyy.liquip.core.util.impl.BasicRegistry;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class LiquipProvider {
    public static final String DEFAULT_NAMESPACE = "minecraft";
    private final Registry<Feature> features;
    private final Registry<ModifierSupplier> modifiers;
    private final Registry<LiquipEnchantment> enchantments;
    private Registry<LiquipItem> items;
    private CraftingRegistry recipes;
    private final ConfigLoader configLoader;

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
        final Status<Void> configResult = configLoader.loadConfig();
        for (Warning warning : configResult.getWarnings()) {
            warning.print(Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger());
        }
        if (configResult.isErr()) {
            configResult.unwrapErr().print(Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger());
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger().error("Could not load config successfully");
            Bukkit.getPluginManager().disablePlugin(Liquip.getProvidingPlugin(Liquip.class));
        } else {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger().info("Successfully loaded config");
        }
    }

    public void reload() {
        items = new BasicRegistry<>();
        recipes = new CraftingRegistry();
        final Status<Void> configResult = configLoader.loadConfig();
        for (Warning warning : configResult.getWarnings()) {
            warning.print(Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger());
        }
        if (configResult.isErr()) {
            configResult.unwrapErr().print(Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger());
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger().error("Could not reload config successfully");
            Bukkit.getPluginManager().disablePlugin(Liquip.getProvidingPlugin(Liquip.class));
        } else {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger().info("Successfully reloaded config");
        }
    }
}
