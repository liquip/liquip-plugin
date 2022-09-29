package com.github.sqyyy.liquip.items;

import com.github.sqyyy.liquip.Liquip;
import com.github.sqyyy.liquip.util.Identifier;
import com.github.sqyyy.liquip.util.Result;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

import java.util.ArrayList;
import java.util.List;

public class LiquipItem {
    private final Identifier key;
    private final Component name;
    private final Material material;
    private final List<Component> lore;
    private final List<LeveledEnchantment> enchantments;
    private final List<Identifier> features;

    public LiquipItem(Identifier key, Component name, Material material, List<Component> lore,
                      List<LeveledEnchantment> enchantments, List<Identifier> features) {
        this.key = key;
        this.name = name;
        this.material = material;
        this.lore = lore;
        this.enchantments = List.copyOf(enchantments);
        this.features = List.copyOf(features);
    }

    public static Result<LiquipItem, LiquipError> fromConfig(Config config) {
        final var miniMessage = MiniMessage.miniMessage();
        Identifier key;
        Component name;
        Material material;
        List<Component> lore = List.of();
        List<LeveledEnchantment> enchantments = List.of();
        List<Identifier> features = List.of();

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

            material = Registry.MATERIAL.get(materialKey);

            if (material == null) {
                return Result.err(LiquipError.INVALID_MATERIAL);
            }
            if (config.hasPath("lore")) {
                final var loreResult = config.getStringList("lore");
                lore = new ArrayList<>();

                for (String line : loreResult) {
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

                    enchantments.add(new LeveledEnchantment(enchantmentId.unwrap(), enchantmentLevel));
                }
            }
        } catch (ConfigException.WrongType wrongType) {
            return Result.err(LiquipError.WRONG_TYPE);
        }

        return Result.ok(new LiquipItem(key, name, material, lore, enchantments, features));
    }

    public Identifier getKey() {
        return key;
    }

    public Component getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public List<Component> getLore() {
        return lore;
    }

    public List<LeveledEnchantment> getEnchantments() {
        return enchantments;
    }

    public List<Identifier> getFeatures() {
        return features;
    }

    public static class Builder {
        private Identifier key = null;
        private Component name = null;
        private Material material = null;
        private List<Component> lore = new ArrayList<>();
        private List<LeveledEnchantment> enchantments = new ArrayList<>();
        private List<Identifier> features = new ArrayList<>();

        public Builder() {
        }

        public Builder(Identifier key, String name, Material material) {
            this.key = key;
            this.name = MiniMessage.miniMessage().deserialize(name);
            this.material = material;
        }

        public Builder identifier(Identifier identifier) {
            this.key = identifier;
            return this;
        }

        public Builder name(String name) {
            this.name = MiniMessage.miniMessage().deserialize(name);
            return this;
        }

        public Builder name(Component name) {
            this.name = name;
            return this;
        }

        public Builder material(Material material) {
            this.material = material;
            return this;
        }

        public Builder lore(List<Component> lore) {
            if (lore == null) {
                this.lore = new ArrayList<>();
                return this;
            }
            this.lore = lore;
            return this;
        }

        public Builder loreLine(Component line) {
            if (line == null) {
                lore.add(Component.newline());
                return this;
            }
            lore.add(line);
            return this;
        }

        public Builder enchantments(List<LeveledEnchantment> enchantments) {
            if (enchantments == null) {
                this.enchantments = new ArrayList<>();
                return this;
            }
            this.enchantments = enchantments;
            return this;
        }

        public Builder enchantment(LeveledEnchantment enchantment) {
            if (enchantment == null) {
                return this;
            }
            enchantments.add(enchantment);
            return this;
        }

        public Builder features(List<Identifier> features) {
            if (features == null) {
                this.features = new ArrayList<>();
                return this;
            }
            this.features = features;
            return this;
        }

        public Builder feature(Identifier feature) {
            if (feature == null) {
                return this;
            }
            features.add(feature);
            return this;
        }

        public LiquipItem build() {
            if (key == null) {
                return null;
            }
            if (name == null) {
                return null;
            }
            if (material == null) {
                return null;
            }
            return new LiquipItem(key, name, material, lore, enchantments, features);
        }
    }
}
