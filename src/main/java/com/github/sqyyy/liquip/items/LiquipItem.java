package com.github.sqyyy.liquip.items;

import com.github.sqyyy.liquip.Liquip;
import com.github.sqyyy.liquip.items.impl.BasicLiquipItem;
import com.github.sqyyy.liquip.system.IgnoredError;
import com.github.sqyyy.liquip.system.LiquipError;
import com.github.sqyyy.liquip.util.Identifier;
import com.github.sqyyy.liquip.util.Registry;
import com.github.sqyyy.liquip.util.Status;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface LiquipItem {
    static Status<LiquipItem> fromConfig(Config config, Registry<LiquipEnchantment> enchantmentRegistry,
                                              Registry<Feature> featureRegistry) {
        final var status = new Status<LiquipItem>();
        final var miniMessage = MiniMessage.miniMessage();
        Identifier id;
        Component name;
        Material material;
        List<Component> lore = List.of();
        List<LeveledEnchantment> enchantments = List.of();
        List<Feature> features = List.of();

        if (!config.hasPath("id")) {
            status.setError(LiquipError.NO_ID_FOUND);
            return status;
        }
        if (!config.hasPath("name")) {
            status.setError(LiquipError.NO_NAME_FOUND);
            return status;
        }
        if (!config.hasPath("material")) {
            status.setError(LiquipError.NO_MATERIAL_FOUND);
            return status;
        }
        try {
            final var keyResult = Identifier.parse(config.getString("id"), Liquip.DEFAULT_NAMESPACE);

            if (keyResult.isErr()) {
                status.setError(LiquipError.INVALID_ID);
                return status;
            }

            id = keyResult.unwrap();
            name = miniMessage.deserialize(config.getString("name"));
            final var materialKey = NamespacedKey.fromString(config.getString("material"));

            if (materialKey == null) {
                status.setError(LiquipError.INVALID_MATERIAL);
                return status;
            }

            material = org.bukkit.Registry.MATERIAL.get(materialKey);

            if (material == null) {
                status.setError(LiquipError.INVALID_MATERIAL);
                return status;
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
                        status.addWarning(new IgnoredError(LiquipError.INVALID_ENCHANTMENT));
                        continue;
                    }

                    final var enchantmentIdResult = enchantment.getString("id");
                    final var enchantmentLevel = enchantment.getInt("level");
                    final var enchantmentId = Identifier.parse(enchantmentIdResult, Liquip.DEFAULT_NAMESPACE);

                    if (enchantmentId.isErr()) {
                        status.addWarning(new IgnoredError(LiquipError.INVALID_ENCHANTMENT));
                        continue;
                    }

                    final var enchantmentResult = LeveledEnchantment.parse(enchantmentId.unwrap(), enchantmentLevel,
                            enchantmentRegistry);

                    if (enchantmentResult.isErr()) {
                        status.addWarning(new IgnoredError("Could not load enchantment '" + enchantmentId + "'",
                                enchantmentResult.unwrapErr()));
                        continue;
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
                        status.addWarning(new IgnoredError(LiquipError.INVALID_FEATURE));
                        continue;
                    }

                    final var featureIdResult = featureId.unwrap();

                    if (!featureRegistry.isRegistered(featureIdResult)) {
                        status.addWarning(
                                new IgnoredError("Feature '" + featureIdResult + "'", LiquipError.FEATURE_NOT_FOUND));
                        continue;
                    }

                    final var featureResult = featureRegistry.get(featureIdResult);
                    features.add(featureResult);
                }
            }
        } catch (ConfigException.WrongType wrongType) {
            status.setError(LiquipError.WRONG_TYPE);
            return status;
        }

        status.setOk(true);
        status.setValue(new BasicLiquipItem(id, name, material, lore, enchantments, features, HashMultimap.create()));
        return status;
    }

    Identifier getId();

    Component getName();

    Material getMaterial();

    List<Component> getLore();

    List<LeveledEnchantment> getEnchantments();

    List<Feature> getFeatures();

    ItemStack newItem();

    <T extends Event> void callEvent(Class<T> eventClass, T event);

    <T extends Event> void registerEvent(Class<T> eventClass, Consumer<T> eventHandler);

    class Builder {
        private Identifier key = null;
        private Component name = null;
        private Material material = null;
        private List<Component> lore = new ArrayList<>();
        private List<LeveledEnchantment> enchantments = new ArrayList<>();
        private List<Feature> features = new ArrayList<>();

        private Multimap<Class<? extends Event>, Consumer<? extends Event>> events = HashMultimap.create();

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

        public Builder features(List<Feature> features) {
            if (features == null) {
                this.features = new ArrayList<>();
                return this;
            }
            this.features = features;
            return this;
        }

        public Builder feature(Feature feature) {
            if (feature == null) {
                return this;
            }
            features.add(feature);
            return this;
        }

        public <T extends Event> Builder event(Class<T> eventClass, Consumer<T> eventConsumer) {
            events.put(eventClass, eventConsumer);
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
            return new BasicLiquipItem(key, name, material, lore, enchantments, features, events);
        }
    }
}
