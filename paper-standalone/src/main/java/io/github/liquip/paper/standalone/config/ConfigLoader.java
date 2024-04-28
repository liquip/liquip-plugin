package io.github.liquip.paper.standalone.config;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.liquip.api.config.ConfigElement;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import io.github.liquip.paper.core.item.FixedItem;
import io.github.liquip.paper.standalone.StandaloneLiquip;
import io.github.liquip.paper.standalone.config.structure.ConfigStructure;
import io.github.liquip.paper.standalone.config.structure.IngredientStructure;
import io.github.liquip.paper.standalone.config.structure.ItemStructure;
import io.github.liquip.paper.standalone.config.structure.RecipeStructure;
import io.github.liquip.paper.standalone.event.CraftingInteractListener;
import io.github.liquip.paper.standalone.item.crafting.StandaloneShapedRecipe;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyedValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Tag;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ConfigLoader {
    private final StandaloneLiquip api;
    private final Logger logger;
    private ConfigStructure config;
    private boolean wasLoadedBefore;

    public ConfigLoader(@NotNull StandaloneLiquip api) {
        Objects.requireNonNull(api);
        this.api = api;
        this.logger = api.getPlugin()
            .getSLF4JLogger();
        this.config = null;
        this.wasLoadedBefore = false;
    }

    public boolean loadConfig() {
        final Path dataDirectory = this.api.getPlugin()
            .getDataFolder()
            .toPath();
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
            this.config = this.api.getMapper()
                .readValue(path.toFile(), ConfigStructure.class);
        } catch (Exception e) {
            this.config = null;
            this.logger.error("Exception whilst loading config file", e);
            return false;
        }
        final List<ItemStructure> items = new ArrayList<>(this.config.getItems()
            .size());
        for (final String item : this.config.getItems()) {
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
                items.add(this.api.getMapper()
                    .readValue(file.toFile(), ItemStructure.class));
            } catch (Exception e) {
                this.logger.error("Exception whilst loading item file", e);
            }
        }
        for (final ItemStructure item : items) {
            final NamespacedKey key = NamespacedKey.fromString(item.getKey());
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
            final Component displayName = Component.text()
                .decoration(TextDecoration.ITALIC, false)
                .append(StandaloneLiquip.MM(item.getDisplayName()))
                .build();
            final List<Component> lore = new ArrayList<>(0);
            if (item.getLore() != null) {
                for (final String loreLine : item.getLore()) {
                    lore.add(Component.text()
                        .decoration(TextDecoration.ITALIC, false)
                        .append(StandaloneLiquip.MM(loreLine))
                        .build());
                }
            }
            final List<Feature> features = new ArrayList<>(0);
            final Map<TaggedFeature<?>, ConfigElement> taggedFeatures = new HashMap<>(0);
            if (item.getFeatures() != null) {
                for (Map.Entry<String, JsonNode> featureEntry : item.getFeatures()
                    .entrySet()) {
                    final JsonNode value = featureEntry.getValue();
                    if (value.isNull()) {
                        this.resolveFeature(featureEntry.getKey())
                            .ifPresentOrElse(features::add,
                                () -> this.logger.warn("Could not get feature '{}' for item '{}', skipping...",
                                    featureEntry.getKey(), key.asString()));
                        continue;
                    }
                    if (value.isBoolean()) {
                        final Optional<Feature> feature = this.resolveFeature(featureEntry.getKey());
                        if (feature.isPresent()) {
                            if (value.asBoolean()) {
                                features.add(feature.get());
                            }
                            continue;
                        }
                    }
                    this.resolveTaggedFeature(featureEntry.getKey())
                        .ifPresentOrElse(it -> taggedFeatures.put(it, new JsonConfigElement(featureEntry.getValue())),
                            () -> this.logger.warn("Could not get tagged feature '{}' for item '{}', skipping...",
                                featureEntry.getKey(), key.asString()));
                }
            }
            final Item itemInstance = new FixedItem.Builder(api).key(key)
                .material(material)
                .name(displayName)
                .lore(lore)
                .features(features)
                .taggedFeatures(taggedFeatures)
                .build();
            api.addConfigItem(itemInstance);
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
                            if (ingredient.getC()
                                .length() != 1 || ingredient.getCount() < 1 || ingredient.getCount() > 64) {
                                this.logger.warn("Could not load recipe with invalid ingredient for item '{}', skipping...",
                                    key.asString());
                                continue recipe;
                            }
                            ingredientMap.put(ingredient.getC()
                                .charAt(0), ingredient);
                        }
                        final List<KeyedValue<Integer>> shape = new ArrayList<>(Collections.nCopies(9, null));
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                final char c = recipeShapeList.get(i)
                                    .charAt(j);
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
                        this.api.addConfigRecipe(new StandaloneShapedRecipe(itemInstance, recipe.getCount(), shape));
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
        }
        Key craftingTableKey = NamespacedKey.fromString(this.config.getCraftingTable());
        if (craftingTableKey == null) {
            this.config = null;
            this.logger.error("Invalid key for crafting table item");
            return false;
        }
        if (!this.config.hasCustomCraftingTable()) {
            final Item defaultCraftingTableItem = this.getDefaultCraftingTable();
            this.api.getItemRegistry()
                .register(defaultCraftingTableItem.key(), defaultCraftingTableItem);
            craftingTableKey = defaultCraftingTableItem.key();
            if (!this.wasLoadedBefore) {
                final ShapedRecipe craftingTableRecipe =
                    new ShapedRecipe((NamespacedKey) defaultCraftingTableItem.key(), defaultCraftingTableItem.newItemStack());
                craftingTableRecipe.shape("aaa", "bbb", "bbb");
                craftingTableRecipe.setIngredient('a', Material.IRON_INGOT);
                craftingTableRecipe.setIngredient('b', new RecipeChoice.MaterialChoice(Tag.PLANKS));
                Bukkit.addRecipe(craftingTableRecipe);
            }
        }
        final Item craftingTableItem = this.api.getItemRegistry()
            .get(craftingTableKey);
        if (craftingTableItem == null) {
            this.config = null;
            this.logger.error("Invalid crafting table item");
            return false;
        }
        if (!this.config.hasCustomCraftingTable()) {
            Bukkit.getPluginManager()
                .registerEvents(new CraftingInteractListener(api), api.getPlugin());
        }
        this.wasLoadedBefore = true;
        return true;
    }

    private @NotNull Optional<Feature> resolveFeature(@NotNull String key) {
        final Key namespacedKey = NamespacedKey.fromString(key);
        if (namespacedKey == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.api.getFeatureRegistry()
            .get(namespacedKey));
    }

    private @NotNull Optional<TaggedFeature<?>> resolveTaggedFeature(@NotNull String key) {
        final Key namespacedKey = NamespacedKey.fromString(key);
        if (namespacedKey == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.api.getTaggedFeatureRegistry()
            .get(namespacedKey));
    }

    private boolean verifyShape(@NotNull List<String> shape) {
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

    private Item getDefaultCraftingTable() {
        return new FixedItem.Builder(api).key(new NamespacedKey("liquip", "crafting_table"))
            .material(Material.CRAFTING_TABLE)
            .name(Component.text("Advanced Crafting Table")
                .decoration(TextDecoration.ITALIC, false))
            .build();
    }

    private void craftingTableOnInteract(@NotNull PlayerInteractEvent event, @Nullable ItemStack item) {
        if (event.getPlayer()
            .getOpenInventory()
            .getTopInventory()
            .getType() != InventoryType.CRAFTING) {
            return;
        }
        event.setCancelled(true);
        this.api.getCraftingUiManager()
            .openCraftingTable(event.getPlayer());
    }
}
