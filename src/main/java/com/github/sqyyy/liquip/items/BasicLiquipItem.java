package com.github.sqyyy.liquip.items;

import com.github.sqyyy.liquip.Liquip;
import com.github.sqyyy.liquip.util.Identifier;
import com.github.sqyyy.liquip.util.Registry;
import com.github.sqyyy.liquip.util.Result;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BasicLiquipItem implements LiquipItem {
    private final Identifier key;
    private final Component name;
    private final Material material;
    private final List<Component> lore;
    private final List<LeveledEnchantment> enchantments;
    private final List<Feature> features;

    public BasicLiquipItem(Identifier key, Component name, Material material, List<Component> lore,
                           List<LeveledEnchantment> enchantments, List<Feature> features) {
        this.key = key;
        this.name = name;
        this.material = material;
        this.lore = List.copyOf(lore);
        this.enchantments = List.copyOf(enchantments);
        this.features = List.copyOf(features);
    }

    public static Result<BasicLiquipItem, LiquipError> fromConfig(Config config,
                                                                  Registry<LiquipEnchantment> enchantmentRegistry,
                                                                  Registry<Feature> featureRegistry) {
        final var miniMessage = MiniMessage.miniMessage();
        Identifier key;
        Component name;
        Material material;
        List<Component> lore = List.of();
        List<LeveledEnchantment> enchantments = List.of();
        List<Feature> features = List.of();

        if (!config.hasPath("key")) {
            return Result.err(LiquipError.NO_KEY_FOUND);
        }
        if (!config.hasPath("name")) {
            return Result.err(LiquipError.NO_NAME_FOUND);
        }
        if (!config.hasPath("material")) {
            return Result.err(LiquipError.NO_MATERIAL_FOUND);
        }

        try {
            final var keyResult = Identifier.parse(config.getString("key"), Liquip.DEFAULT_NAMESPACE);

            if (keyResult.isErr()) {
                return Result.err(LiquipError.INVALID_KEY);
            }

            key = keyResult.unwrap();
            name = miniMessage.deserialize(config.getString("name"));
            final var materialKey = NamespacedKey.fromString(config.getString("material"));

            if (materialKey == null) {
                return Result.err(LiquipError.INVALID_MATERIAL);
            }

            material = org.bukkit.Registry.MATERIAL.get(materialKey);

            if (material == null) {
                return Result.err(LiquipError.INVALID_MATERIAL);
            }
            if (config.hasPath("lore")) {
                final var loreResult = config.getStringList("lore");
                lore = new ArrayList<>();

                for (final var line : loreResult) {
                    lore.add(miniMessage.deserialize(line));
                }
            }
            if (config.hasPath("enchantments")) {
                final var enchantmentsResult = config.getConfigList("enchantments");
                enchantments = new ArrayList<>();

                for (final var enchantment : enchantmentsResult) {
                    if (!enchantment.hasPath("id") || !enchantment.hasPath("level")) {
                        return Result.err(LiquipError.INVALID_ENCHANTMENT);
                    }

                    final var enchantmentIdResult = enchantment.getString("id");
                    final var enchantmentLevel = enchantment.getInt("level");
                    final var enchantmentId = Identifier.parse(enchantmentIdResult, Liquip.DEFAULT_NAMESPACE);

                    if (enchantmentId.isErr()) {
                        return Result.err(LiquipError.INVALID_ENCHANTMENT);
                    }

                    final var enchantmentResult = LeveledEnchantment.parse(enchantmentId.unwrap(), enchantmentLevel,
                            enchantmentRegistry);

                    if (enchantmentResult.isErr()) {
                        return Result.err(enchantmentResult.unwrapErr());
                    }

                    enchantments.add(enchantmentResult.unwrap());
                }
            }
            if (config.hasPath("features")) {
                final var featuresResult = config.getStringList("features");
                features = new ArrayList<>();

                for (final var feature : featuresResult) {
                    final var featureId = Identifier.parse(feature, Liquip.DEFAULT_NAMESPACE);

                    if (featureId.isErr()) {
                        return Result.err(LiquipError.INVALID_FEATURE);
                    }

                    final var featureIdResult = featureId.unwrap();

                    if (!featureRegistry.isRegistered(featureIdResult)) {
                        continue;
                    }

                    final var featureResult = featureRegistry.get(featureIdResult);

                    features.add(featureResult);
                }
            }
        } catch (ConfigException.WrongType wrongType) {
            return Result.err(LiquipError.WRONG_TYPE);
        }

        return Result.ok(new BasicLiquipItem(key, name, material, lore, enchantments, features));
    }

    @Override
    public Identifier getKey() {
        return key;
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public List<Component> getLore() {
        return lore;
    }

    @Override
    public List<LeveledEnchantment> getEnchantments() {
        return enchantments;
    }

    @Override
    public List<Feature> getFeatures() {
        return features;
    }

    @Override
    public ItemStack newItem() {
        final var itemStack = new ItemStack(material);
        final var itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(name);
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        for (LeveledEnchantment enchantment : enchantments) {
            enchantment.apply(itemStack);
        }
        return itemStack;
    }
}
