package com.github.sqyyy.liquip.core.items;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.items.impl.BasicLiquipItem;
import com.github.sqyyy.liquip.core.system.IgnoredError;
import com.github.sqyyy.liquip.core.system.LiquipError;
import com.github.sqyyy.liquip.core.system.SimpleWarning;
import com.github.sqyyy.liquip.core.system.craft.CraftingHashObject;
import com.github.sqyyy.liquip.core.system.craft.CraftingRegistry;
import com.github.sqyyy.liquip.core.system.craft.ShapedCraftingRecipe;
import com.github.sqyyy.liquip.core.system.craft.ShapelessCraftingRecipe;
import com.github.sqyyy.liquip.core.util.*;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public interface LiquipItem {
    static Status<LiquipItem> fromConfig(Config config, Registry<LiquipEnchantment> enchantmentRegistry,
                                         Registry<Feature> featureRegistry, CraftingRegistry craftingRegistry) {
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
            name = name.decoration(TextDecoration.ITALIC, false);
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
                    lore.add(miniMessage.deserialize(line).decoration(TextDecoration.ITALIC, false));
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
                    final var enchantmentResult =
                            LeveledEnchantment.parse(enchantmentId.unwrap(), enchantmentLevel, enchantmentRegistry);
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
            if (config.hasPath("recipes")) {
                final var recipesResult = config.getConfigList("recipes");
                recipe:
                for (final var recipe : recipesResult) {
                    var shapeless = false;
                    if (recipe.hasPath("shapeless")) {
                        shapeless = recipe.getBoolean("shapeless");
                    }
                    if (shapeless) {
                        if (!recipe.hasPath("ingredients")) {
                            status.addWarning(new SimpleWarning("Shapeless recipe has no ingredients tag"));
                            continue;
                        }
                        final var ingredientIds = new Identifier[9];
                        final var ingredientCounts = new Integer[9];
                        final var ingredientsResult = recipe.getConfigList("ingredients");
                        var index = 0;
                        for (final var ingredient : ingredientsResult) {
                            if (!ingredient.hasPath("id")) {
                                status.addWarning(new SimpleWarning("Ingredient of shapeless recipe has no id tag"));
                                continue recipe;
                            }
                            final var ingredientIdResult = ingredient.getString("id");
                            final var ingredientId = Identifier.parse(ingredientIdResult);
                            if (ingredientId.isErr()) {
                                status.addWarning(new SimpleWarning("Ingredient of shapeless recipe has invalid id"));
                                continue recipe;
                            }
                            ingredientCounts[index] = 0;
                            if (ingredient.hasPath("count")) {
                                ingredientCounts[index] = ingredient.getInt("count");
                            }
                            ingredientIds[index] = ingredientId.unwrap();
                            index++;
                            if (index == 9) {
                                break;
                            }
                        }
                        final var craftingHashObject = new CraftingHashObject(ingredientIds, false);
                        Bukkit.broadcast(Component.text("registered new recipe " + craftingHashObject.hashCode()));
                        craftingRegistry.register(craftingHashObject,
                                new ShapelessCraftingRecipe(Arrays.asList(ingredientCounts),
                                        Arrays.asList(ingredientIds), id));
                        continue;
                    }
                    if (!recipe.hasPath("shape")) {
                        status.addWarning(new SimpleWarning("Shaped recipe has no shape tag"));
                        continue;
                    }
                    if (!recipe.hasPath("placeholders")) {
                        status.addWarning(new SimpleWarning("Shaped recipe has no placeholders tag"));
                        continue;
                    }
                    final var shapeStrings = recipe.getStringList("shape");
                    final var placeholders = recipe.getConfigList("placeholders");
                    final var placeholderMap = new HashMap<Character, ShapedCraftingRecipe.Placeholder>();
                    placeholderMap.put(' ',
                            new ShapedCraftingRecipe.Placeholder(' ', new Identifier("minecraft", "air"), 0));
                    final var shape = new char[9];
                    for (int i = 0; i < shapeStrings.size(); i++) {
                        final var shapeString = shapeStrings.get(i);
                        if (shapeString.length() < 3) {
                            status.addWarning(new SimpleWarning("Shaped recipe invalid shape"));
                            continue recipe;
                        }
                        shape[i * 3] = shapeString.charAt(0);
                        shape[i * 3 + 1] = shapeString.charAt(1);
                        shape[i * 3 + 2] = shapeString.charAt(2);
                        if (i == 2) {
                            break;
                        }
                    }
                    if (shapeStrings.size() < 3) {
                        status.addWarning(new SimpleWarning("Shaped recipe has too short shape tag"));
                        continue;
                    }
                    for (final var placeholder : placeholders) {
                        if (!placeholder.hasPath("key")) {
                            status.addWarning(new SimpleWarning("Shaped recipe has no placeholder key"));
                            continue recipe;
                        }
                        if (!placeholder.hasPath("type")) {
                            status.addWarning(new SimpleWarning("Shaped recipe has no placeholder type"));
                            continue recipe;
                        }
                        final var keyString = placeholder.getString("key");
                        if (keyString.isEmpty()) {
                            status.addWarning(new SimpleWarning("Shaped recipe has invalid placeholder key"));
                            continue recipe;
                        }
                        final var key = keyString.charAt(0);
                        final var typeResult = Identifier.parse(placeholder.getString("type"));
                        if (typeResult.isErr()) {
                            status.addWarning(new SimpleWarning("Shaped recipe has invalid placeholder type"));
                            continue recipe;
                        }
                        var count = 1;
                        if (placeholder.hasPath("count")) {
                            count = placeholder.getInt("count");
                        }
                        placeholderMap.put(key, new ShapedCraftingRecipe.Placeholder(key, typeResult.unwrap(), count));
                    }
                    final var ingredientIds = new Identifier[9];
                    final var ingredientCounts = new int[9];
                    for (int i = 0; i < shape.length; i++) {
                        if (!placeholderMap.containsKey(shape[i])) {
                            status.addWarning(
                                    new SimpleWarning("Shaped recipe is missing '" + shape[i] + "' placeholder"));
                            continue recipe;
                        }
                        final var placeholder = placeholderMap.get(shape[i]);
                        ingredientIds[i] = placeholder.getType();
                        ingredientCounts[i] = placeholder.getCount();
                    }
                    final var hash = new CraftingHashObject(ingredientIds, true);
                    craftingRegistry.register(hash, new ShapedCraftingRecipe(ingredientCounts, ingredientIds, id));
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

    static Identifier getIdentifier(@NotNull ItemStack itemStack) {
        // TODO - version dependent
        final org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack craftItemStack =
                itemStack instanceof org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack it ? it :
                        org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack.asCraftCopy(itemStack);
        final net.minecraft.world.item.ItemStack nmsHandle = craftItemStack.handle != null ? craftItemStack.handle :
                org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack.asNMSCopy(itemStack);
        if (!nmsHandle.hasTag()) {
            final NamespacedKey key = itemStack.getType().getKey();
            return new Identifier(key.getNamespace(), key.getKey());
        }
        final net.minecraft.nbt.CompoundTag nmsTag = nmsHandle.tag;
        if (!nmsTag.contains("liquip:identifier")) {
            final NamespacedKey key = itemStack.getType().getKey();
            return new Identifier(key.getNamespace(), key.getKey());
        }
        Result<Identifier, UtilError> identifierResult = Identifier.parse(nmsTag.getString("liquip:identifier"));
        if (identifierResult.isErr()) {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger()
                    .warn("A player has an item with an invalid identifier tag");
            final NamespacedKey key = itemStack.getType().getKey();
            return new Identifier(key.getNamespace(), key.getKey());
        }
        return identifierResult.unwrap();
    }

    static @NotNull ItemStack setIdentifier(@NotNull ItemStack itemStack, @NotNull Identifier identifier) {
        // TODO - version dependent
        final org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack craftItemStack =
                itemStack instanceof org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack it ? it :
                        org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack.asCraftCopy(itemStack);
        final net.minecraft.world.item.ItemStack nmsHandle = craftItemStack.handle;
        final net.minecraft.nbt.CompoundTag nmsTag = nmsHandle.getOrCreateTag();
        nmsTag.putString("liquip:identifier", identifier.toString());
        return craftItemStack;
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
        private final Multimap<Class<? extends Event>, Consumer<? extends Event>> events = HashMultimap.create();
        private Identifier key = null;
        private Component name = null;
        private Material material = null;
        private List<Component> lore = new ArrayList<>();
        private List<LeveledEnchantment> enchantments = new ArrayList<>();
        private List<Feature> features = new ArrayList<>();

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
