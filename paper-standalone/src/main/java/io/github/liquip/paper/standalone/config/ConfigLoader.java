package io.github.liquip.paper.standalone.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ArrayListMultimap;
import io.github.liquip.api.config.ConfigElement;
import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.TaggedFeature;
import io.github.liquip.paper.core.item.ItemImpl;
import io.github.liquip.paper.standalone.StandaloneLiquipImpl;
import io.github.liquip.paper.standalone.config.structure.ConfigStructure;
import io.github.liquip.paper.standalone.config.structure.EnchantmentStructure;
import io.github.liquip.paper.standalone.config.structure.ItemStructure;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("PatternValidation")
public class ConfigLoader {
    private final StandaloneLiquipImpl api;
    private ConfigStructure config = null;
    private List<ItemStructure> items = null;

    public ConfigLoader(StandaloneLiquipImpl api) {
        this.api = api;
    }

    public boolean loadConfig() {
        final Path dataDirectory = this.api.getPlugin().getDataFolder().toPath();
        if (!Files.exists(dataDirectory)) {
            try {
                Files.createDirectories(dataDirectory);
            } catch (IOException e) {
                this.config = null;
                this.api.getPlugin().getSLF4JLogger()
                    .error("Exception whilst creating plugin directory", e);
                return false;
            }
        }
        if (!Files.isDirectory(dataDirectory)) {
            this.config = null;
            this.api.getPlugin().getSLF4JLogger()
                .error("Plugin path is a file, should instead be a directory");
            return false;
        }
        final Path path = dataDirectory.resolve("config.json");
        if (!Files.exists(path)) {
            try {
                Files.writeString(path, """
                    {
                        items: []
                    }""");
            } catch (IOException e) {
                this.config = null;
                this.api.getPlugin().getSLF4JLogger()
                    .error("Exception whilst creating config file", e);
                return false;
            }
        }
        try {
            this.config = this.api.getMapper().readValue(path.toFile(), ConfigStructure.class);
        } catch (Exception e) {
            this.config = null;
            this.api.getPlugin().getSLF4JLogger().error("Exception whilst loading config file", e);
            return false;
        }
        this.items = new ArrayList<>(this.config.getItems().size());
        for (String item : this.config.getItems()) {
            if (item.contains("..")) {
                this.api.getPlugin().getSLF4JLogger()
                    .warn("Path '{}' for item is backwards relative, skipping...", item);
                continue;
            }
            final Path file = dataDirectory.resolve(item);
            if (!Files.exists(file)) {
                this.api.getPlugin().getSLF4JLogger()
                    .warn("Path '{}' for item does not exist, skipping...", item);
                continue;
            }
            if (!Files.isRegularFile(file)) {
                this.api.getPlugin().getSLF4JLogger()
                    .warn("Path '{}' for item is not a file, skipping...", item);
                continue;
            }
            try {
                this.items.add(this.api.getMapper().readValue(file.toFile(), ItemStructure.class));
            } catch (Exception e) {
                this.api.getPlugin().getSLF4JLogger()
                    .error("Exception whilst loading item file", e);
            }
        }
        for (final ItemStructure item : this.items) {
            try {
                final Key key = Key.key(item.getKey());
                final Key materialKey = Key.key(item.getMaterial());
                final Material material = Registry.MATERIAL.get(
                    new NamespacedKey(materialKey.namespace(), materialKey.value()));
                if (material == null) {
                    this.api.getPlugin().getSLF4JLogger()
                        .warn("Could not get material for item '{}', skipping...", key.asString());
                    continue;
                }
                final Component displayName =
                    Component.text().decoration(TextDecoration.ITALIC, false)
                        .append(StandaloneLiquipImpl.MM(item.getDisplayName())).build();
                final List<Component> lore = new ArrayList<>(0);
                if (item.getLore() != null) {
                    for (final String loreLine : item.getLore()) {
                        lore.add(Component.text().decoration(TextDecoration.ITALIC, false)
                            .append(StandaloneLiquipImpl.MM(loreLine)).build());
                    }
                }
                final Object2IntMap<Enchantment> enchantments = new Object2IntOpenHashMap<>();
                if (item.getEnchantments() != null) {
                    for (final EnchantmentStructure enchantment : item.getEnchantments()) {
                        this.resolveEnchantment(enchantment.getId())
                            .ifPresentOrElse(it -> enchantments.put(it, enchantment.getLevel()),
                                () -> this.api.getPlugin().getSLF4JLogger().warn(
                                    "Could not get enchantment '{}' for item '{}', skipping...",
                                    enchantment.getId(), key.asString()));
                    }
                }
                final List<Feature> features = new ArrayList<>(0);
                final Map<TaggedFeature<?>, ConfigElement> taggedFeatures = new HashMap<>(0);
                if (item.getFeatures() != null) {
                    for (Map.Entry<String, JsonNode> feature : item.getFeatures().entrySet()) {
                        if (feature.getValue() == null || feature.getValue().isNull()) {
                            this.resolveFeature(feature.getKey()).ifPresentOrElse(features::add,
                                () -> this.api.getPlugin().getSLF4JLogger()
                                    .warn("Could not get feature '{}' for item '{}', skipping...",
                                        feature.getKey(), key.asString()));
                        } else {
                            this.resolveTaggedFeature(feature.getKey()).ifPresentOrElse(
                                it -> taggedFeatures.put(it,
                                    new JsonConfigElement(feature.getValue())),
                                () -> this.api.getPlugin().getSLF4JLogger().warn(
                                    "Could not get tagged feature '{}' for item '{}', skipping...",
                                    feature.getKey(), key.asString()));
                        }
                    }
                }
                this.api.getPlugin().getSLF4JLogger().info("Registering...");
                this.api.getItemRegistry().register(key,
                    new ItemImpl(this.api, key, material, displayName, lore, enchantments, features,
                        taggedFeatures, ArrayListMultimap.create()));
            } catch (InvalidKeyException e) {
                this.api.getPlugin().getSLF4JLogger().error("Exception whilst parsing key", e);
            }
        }
        return true;
    }

    private Optional<Enchantment> resolveEnchantment(@NonNull String key) {
        try {
            return Optional.ofNullable(this.api.getEnchantmentRegistry().get(Key.key(key)));
        } catch (InvalidKeyException e) {
            this.api.getPlugin().getSLF4JLogger().error("Exception whilst parsing key", e);
            return Optional.empty();
        }
    }

    private Optional<Feature> resolveFeature(@NonNull String key) {
        try {
            return Optional.ofNullable(this.api.getFeatureRegistry().get(Key.key(key)));
        } catch (InvalidKeyException e) {
            this.api.getPlugin().getSLF4JLogger().error("Exception whilst parsing key", e);
            return Optional.empty();
        }
    }

    private Optional<TaggedFeature<?>> resolveTaggedFeature(@NonNull String key) {
        try {
            return Optional.ofNullable(this.api.getTaggedFeatureRegistry().get(Key.key(key)));
        } catch (InvalidKeyException e) {
            this.api.getPlugin().getSLF4JLogger().error("Exception whilst parsing key", e);
            return Optional.empty();
        }
    }

    public ConfigStructure getConfig() {
        return this.config;
    }

    public List<ItemStructure> getItems() {
        return this.items;
    }
}
