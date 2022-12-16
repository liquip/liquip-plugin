package io.github.liquip.paper.standalone.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ArrayListMultimap;
import io.github.liquip.api.config.ConfigElement;
import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.TaggedFeature;
import io.github.liquip.paper.core.item.ItemImpl;
import io.github.liquip.paper.standalone.StandaloneLiquipImpl;
import io.github.liquip.paper.standalone.config.structure.*;
import io.github.liquip.paper.standalone.item.crafting.ShapedRecipeImpl;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyedValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConfigLoader {
    private final StandaloneLiquipImpl api;
    private final Logger logger;
    private ConfigStructure config = null;
    private List<ItemStructure> items = null;

    public ConfigLoader(@NonNull StandaloneLiquipImpl api) {
        this.api = api;
        this.logger = api.getPlugin().getSLF4JLogger();
    }

    public boolean loadConfig() {
        final Path dataDirectory = this.api.getPlugin().getDataFolder().toPath();
        if (!Files.exists(dataDirectory)) {
            try {
                Files.createDirectories(dataDirectory);
            } catch (IOException e) {
                this.config = null;
                this.logger.error("Exception whilst creating plugin directory", e);
                return false;
            }
        }
        if (!Files.isDirectory(dataDirectory)) {
            this.config = null;
            this.logger.error("Plugin path is a file, should instead be a directory");
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
                this.logger.error("Exception whilst creating config file", e);
                return false;
            }
        }
        try {
            this.config = this.api.getMapper().readValue(path.toFile(), ConfigStructure.class);
        } catch (Exception e) {
            this.config = null;
            this.logger.error("Exception whilst loading config file", e);
            return false;
        }
        this.items = new ArrayList<>(this.config.getItems().size());
        for (String item : this.config.getItems()) {
            if (item.contains("..")) {
                this.logger.warn("Path '{}' for item is backwards relative, skipping...", item);
                continue;
            }
            final Path file = dataDirectory.resolve(item);
            if (!Files.exists(file)) {
                this.logger.warn("Path '{}' for item does not exist, skipping...", item);
                continue;
            }
            if (!Files.isRegularFile(file)) {
                this.logger.warn("Path '{}' for item is not a file, skipping...", item);
                continue;
            }
            try {
                this.items.add(this.api.getMapper().readValue(file.toFile(), ItemStructure.class));
            } catch (Exception e) {
                this.logger.error("Exception whilst loading item file", e);
            }
        }
        for (final ItemStructure item : this.items) {
            try {
                final Key key = NamespacedKey.fromString(item.getKey());
                if (key == null) {
                    this.logger.warn("Could not get key for item '{}', skipping...", item.getKey());
                    continue;
                }
                final Key materialKey = NamespacedKey.fromString(item.getMaterial());
                if (materialKey == null) {
                    this.logger.warn("Could not get material key for item '{}', skipping...", key.asString());
                    continue;
                }
                final Material material = Registry.MATERIAL.get(new NamespacedKey(materialKey.namespace(), materialKey.value()));
                if (material == null) {
                    this.logger.warn("Could not get material for item '{}', skipping...", key.asString());
                    continue;
                }
                final Component displayName = Component.text().decoration(TextDecoration.ITALIC, false)
                    .append(StandaloneLiquipImpl.MM(item.getDisplayName())).build();
                final List<Component> lore = new ArrayList<>(0);
                if (item.getLore() != null) {
                    for (final String loreLine : item.getLore()) {
                        lore.add(
                            Component.text().decoration(TextDecoration.ITALIC, false).append(StandaloneLiquipImpl.MM(loreLine))
                                .build());
                    }
                }
                final Object2IntMap<Enchantment> enchantments = new Object2IntOpenHashMap<>();
                if (item.getEnchantments() != null) {
                    for (final EnchantmentStructure enchantment : item.getEnchantments()) {
                        this.resolveEnchantment(enchantment.getId())
                            .ifPresentOrElse(it -> enchantments.put(it, enchantment.getLevel()),
                                () -> this.logger.warn("Could not get enchantment '{}' for item '{}', skipping...",
                                    enchantment.getId(), key.asString()));
                    }
                }
                final List<Feature> features = new ArrayList<>(0);
                final Map<TaggedFeature<?>, ConfigElement> taggedFeatures = new HashMap<>(0);
                if (item.getFeatures() != null) {
                    for (Map.Entry<String, JsonNode> feature : item.getFeatures().entrySet()) {
                        if (feature.getValue() == null || feature.getValue().isNull()) {
                            this.resolveFeature(feature.getKey()).ifPresentOrElse(features::add,
                                () -> this.logger.warn("Could not get feature '{}' for item '{}', skipping...", feature.getKey(),
                                    key.asString()));
                        } else {
                            this.resolveTaggedFeature(feature.getKey())
                                .ifPresentOrElse(it -> taggedFeatures.put(it, new JsonConfigElement(feature.getValue())),
                                    () -> this.logger.warn("Could not get tagged feature '{}' for item '{}', skipping...",
                                        feature.getKey(), key.asString()));
                        }
                    }
                }
                final ItemImpl itemInstance =
                    new ItemImpl(this.api, key, material, displayName, lore, enchantments, features, taggedFeatures,
                        ArrayListMultimap.create());
                this.logger.info("Registering...");
                this.api.getItemRegistry().register(key, itemInstance);
                if (item.getRecipes() != null) {
                    recipe:
                    for (final RecipeStructure recipe : item.getRecipes()) {
                        final List<String> recipeShapeList = recipe.getShape();
                        if (recipeShapeList != null) {
                            if (!this.verifyShape(recipeShapeList)) {
                                this.logger.warn("Could not load recipe with invalid shape for item '{}', skipping...",
                                    key.asString());
                                continue;
                            }
                            final Char2ObjectMap<IngredientStructure> ingredientMap = new Char2ObjectOpenHashMap<>();
                            for (final IngredientStructure ingredient : recipe.getIngredients()) {
                                if (ingredient.getC().length() != 1 || ingredient.getCount() < 1 || ingredient.getCount() > 64) {
                                    this.logger.warn("Could not load recipe with invalid ingredient for item '{}', skipping...",
                                        key.asString());
                                    continue recipe;
                                }
                                ingredientMap.put(ingredient.getC().charAt(0), ingredient);
                            }
                            final List<KeyedValue<Integer>> shape = new ArrayList<>(Collections.nCopies(9, null));
                            for (int i = 0; i < 3; i++) {
                                for (int j = 0; j < 3; j++) {
                                    final char c = recipeShapeList.get(i).charAt(j);
                                    if (c == ' ') {
                                        shape.set(i * 3 + j, null);
                                        continue;
                                    }
                                    final IngredientStructure ingredientStructure = ingredientMap.get(c);
                                    if (ingredientStructure == null) {
                                        this.logger.warn("Could not load recipe with invalid shape for item '{}', skipping...",
                                            key.asString());
                                        continue recipe;
                                    }
                                    final Key ingredientKey = NamespacedKey.fromString(ingredientStructure.getKey());
                                    if (ingredientKey == null) {
                                        this.logger.warn(
                                            "Could not load recipe with invalid ingredient key for item '{}', skipping...",
                                            key.asString());
                                        continue recipe;
                                    }
                                    shape.set(i * 3 + j, KeyedValue.keyedValue(ingredientKey, ingredientStructure.getCount()));
                                }
                            }
                            this.api.getCraftingSystem().registerShapedRecipe(new ShapedRecipeImpl(itemInstance, shape));
                            continue;
                        }
                        this.logger.warn("Shapeless crafting is not implemented yet");
                        // TODO - implement
                        //final Set<KeyedValue<Integer>> ingredients = new HashSet<>();
                        //this.api.getCraftingSystem()
                        //    .registerShapelessRecipe(new ShapelessRecipeImpl(itemInstance,
                        //    ingredients));
                    }
                }
            } catch (InvalidKeyException e) {
                this.logger.error("Exception whilst parsing key", e);
            }
        }
        return true;
    }

    private @NonNull Optional<Enchantment> resolveEnchantment(@NonNull String key) {
        try {
            final Key namespacedKey = NamespacedKey.fromString(key);
            if (namespacedKey == null) {
                return Optional.empty();
            }
            return Optional.ofNullable(this.api.getEnchantmentRegistry().get(namespacedKey));
        } catch (InvalidKeyException e) {
            this.logger.error("Exception whilst parsing key", e);
            return Optional.empty();
        }
    }

    private @NonNull Optional<Feature> resolveFeature(@NonNull String key) {
        final Key namespacedKey = NamespacedKey.fromString(key);
        if (namespacedKey == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.api.getFeatureRegistry().get(namespacedKey));
    }

    private @NonNull Optional<TaggedFeature<?>> resolveTaggedFeature(@NonNull String key) {
        final Key namespacedKey = NamespacedKey.fromString(key);
        if (namespacedKey == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.api.getTaggedFeatureRegistry().get(namespacedKey));
    }

    private boolean verifyShape(@NonNull List<String> shape) {
        if (shape.size() != 3) {
            return false;
        }
        for (final String row : shape) {
            if (row.length() != 3) {
                return false;
            }
        }
        return true;
    }

    public @Nullable ConfigStructure getConfig() {
        return this.config;
    }

    public @Nullable List<ItemStructure> getItems() {
        return this.items;
    }
}
