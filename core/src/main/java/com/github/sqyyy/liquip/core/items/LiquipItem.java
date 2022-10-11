package com.github.sqyyy.liquip.core.items;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.LiquipProvider;
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
import com.typesafe.config.ConfigValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public interface LiquipItem {
    static @NotNull Status<@NotNull LiquipItem> fromConfig(@NotNull Config config,
                                                           @NotNull Registry<@NotNull LiquipEnchantment> enchantmentRegistry,
                                                           @NotNull Registry<@NotNull Feature> featureRegistry,
                                                           @NotNull Registry<@NotNull ModifierSupplier> modifierRegistry,
                                                           @NotNull CraftingRegistry craftingRegistry) {
        final Status<LiquipItem> status = new Status<LiquipItem>();
        final MiniMessage miniMessage = MiniMessage.miniMessage();
        Identifier id;
        Component name;
        Material material;
        List<Component> lore = List.of();
        List<LeveledEnchantment> enchantments = List.of();
        List<Feature> features = List.of();
        List<Modifier> modifiers = List.of();
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
            final Result<Identifier, UtilError> keyResult =
                    Identifier.parse(config.getString("id"), LiquipProvider.DEFAULT_NAMESPACE);
            if (keyResult.isErr()) {
                status.setError(LiquipError.INVALID_ID);
                return status;
            }
            id = keyResult.unwrap();
            name = miniMessage.deserialize(config.getString("name"));
            name = name.decoration(TextDecoration.ITALIC, false);
            final NamespacedKey materialKey = NamespacedKey.fromString(config.getString("material"));
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
                final List<String> loreResult = config.getStringList("lore");
                lore = new ArrayList<>();
                for (final String line : loreResult)
                    lore.add(miniMessage.deserialize(line).decoration(TextDecoration.ITALIC, false));
            }
            if (config.hasPath("enchantments")) {
                final List<? extends Config> enchantmentsResult = config.getConfigList("enchantments");
                enchantments = new ArrayList<>();
                for (final Config enchantment : enchantmentsResult) {
                    if (!enchantment.hasPath("id") || !enchantment.hasPath("level")) {
                        status.addWarning(new IgnoredError(LiquipError.INVALID_ENCHANTMENT));
                        continue;
                    }
                    final String enchantmentIdResult = enchantment.getString("id");
                    final int enchantmentLevel = enchantment.getInt("level");
                    final Result<Identifier, UtilError> enchantmentId =
                            Identifier.parse(enchantmentIdResult, LiquipProvider.DEFAULT_NAMESPACE);
                    if (enchantmentId.isErr()) {
                        status.addWarning(new IgnoredError(LiquipError.INVALID_ENCHANTMENT));
                        continue;
                    }
                    final Status<LeveledEnchantment> enchantmentResult =
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
                final List<String> featuresResult = config.getStringList("features");
                features = new ArrayList<>();
                for (final String feature : featuresResult) {
                    final Result<Identifier, UtilError> featureId =
                            Identifier.parse(feature, LiquipProvider.DEFAULT_NAMESPACE);
                    if (featureId.isErr()) {
                        status.addWarning(new IgnoredError(LiquipError.INVALID_FEATURE));
                        continue;
                    }
                    final Identifier featureIdResult = featureId.unwrap();
                    if (!featureRegistry.isRegistered(featureIdResult)) {
                        status.addWarning(
                                new IgnoredError("Feature '" + featureIdResult + "'", LiquipError.FEATURE_NOT_FOUND));
                        continue;
                    }
                    final Feature featureResult = featureRegistry.get(featureIdResult);
                    features.add(featureResult);
                }
            }
            if (config.hasPath("modifiers")) {
                final Config modifiersResult = config.getConfig("modifiers");
                modifiers = new ArrayList<>();
                final Set<String> keyAccumulator = new HashSet<>();
                for (Map.Entry<String, ConfigValue> modifiersEntry : modifiersResult.entrySet()) {
                    final String key = modifiersEntry.getKey().split("\\.")[0];
                    if (keyAccumulator.contains(key)) {
                        continue;
                    }
                    keyAccumulator.add(key);
                    final Result<@NotNull Identifier, @NotNull UtilError> modifierId =
                            Identifier.parse(key, LiquipProvider.DEFAULT_NAMESPACE);
                    final Config modifier = modifiersResult.getConfig(key);
                    if (modifierId.isErr()) {
                        status.addWarning(new SimpleWarning("Invalid modifier id"));
                        continue;
                    }
                    final Identifier modifierIdResult = modifierId.unwrap();
                    if (!modifierRegistry.isRegistered(modifierIdResult)) {
                        status.addWarning(new SimpleWarning("Modifier is not registered: " + modifierIdResult));
                        continue;
                    }
                    final Modifier modifierResult = modifierRegistry.get(modifierIdResult).get(modifier);
                    if (modifierResult != null) {
                        modifiers.add(modifierResult);
                    }
                }
            }
            if (config.hasPath("recipes")) {
                final List<? extends Config> recipesResult = config.getConfigList("recipes");
                recipe:
                for (final Config recipe : recipesResult) {
                    boolean shapeless = false;
                    if (recipe.hasPath("shapeless")) {
                        shapeless = recipe.getBoolean("shapeless");
                    }
                    if (shapeless) {
                        if (!recipe.hasPath("ingredients")) {
                            status.addWarning(new SimpleWarning("Shapeless recipe has no ingredients tag"));
                            continue;
                        }
                        final Identifier[] ingredientIds = new Identifier[9];
                        final Integer[] ingredientCounts = new Integer[9];
                        final List<? extends Config> ingredientsResult = recipe.getConfigList("ingredients");
                        int index = 0;
                        for (final Config ingredient : ingredientsResult) {
                            if (!ingredient.hasPath("id")) {
                                status.addWarning(new SimpleWarning("Ingredient of shapeless recipe has no id tag"));
                                continue recipe;
                            }
                            final String ingredientIdResult = ingredient.getString("id");
                            final Result<Identifier, UtilError> ingredientId =
                                    Identifier.parse(ingredientIdResult, LiquipProvider.DEFAULT_NAMESPACE);
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
                        final CraftingHashObject craftingHashObject = new CraftingHashObject(ingredientIds, false);
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
                    final List<String> shapeStrings = recipe.getStringList("shape");
                    final List<? extends Config> placeholders = recipe.getConfigList("placeholders");
                    final HashMap<Character, ShapedCraftingRecipe.Placeholder> placeholderMap = new HashMap<>();
                    placeholderMap.put(' ',
                            new ShapedCraftingRecipe.Placeholder(' ', new Identifier("minecraft", "air"), 0));
                    final char[] shape = new char[9];
                    for (int i = 0; i < shapeStrings.size(); i++) {
                        final String shapeString = shapeStrings.get(i);
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
                    for (final Config placeholder : placeholders) {
                        if (!placeholder.hasPath("key")) {
                            status.addWarning(new SimpleWarning("Shaped recipe has no placeholder key"));
                            continue recipe;
                        }
                        if (!placeholder.hasPath("type")) {
                            status.addWarning(new SimpleWarning("Shaped recipe has no placeholder type"));
                            continue recipe;
                        }
                        final String keyString = placeholder.getString("key");
                        if (keyString.isEmpty()) {
                            status.addWarning(new SimpleWarning("Shaped recipe has invalid placeholder key"));
                            continue recipe;
                        }
                        final char key = keyString.charAt(0);
                        final Result<Identifier, UtilError> typeResult =
                                Identifier.parse(placeholder.getString("type"), LiquipProvider.DEFAULT_NAMESPACE);
                        if (typeResult.isErr()) {
                            status.addWarning(new SimpleWarning("Shaped recipe has invalid placeholder type"));
                            continue recipe;
                        }
                        int count = 1;
                        if (placeholder.hasPath("count")) {
                            count = placeholder.getInt("count");
                        }
                        placeholderMap.put(key, new ShapedCraftingRecipe.Placeholder(key, typeResult.unwrap(), count));
                    }
                    final Identifier[] ingredientIds = new Identifier[9];
                    final int[] ingredientCounts = new int[9];
                    for (int i = 0; i < shape.length; i++) {
                        if (!placeholderMap.containsKey(shape[i])) {
                            status.addWarning(
                                    new SimpleWarning("Shaped recipe is missing '" + shape[i] + "' placeholder"));
                            continue recipe;
                        }
                        final ShapedCraftingRecipe.Placeholder placeholder = placeholderMap.get(shape[i]);
                        ingredientIds[i] = placeholder.getType();
                        ingredientCounts[i] = placeholder.getCount();
                    }
                    final CraftingHashObject hash = new CraftingHashObject(ingredientIds, true);
                    craftingRegistry.register(hash, new ShapedCraftingRecipe(ingredientCounts, ingredientIds, id));
                }
            }
        } catch (ConfigException.WrongType wrongType) {
            status.setError(LiquipError.WRONG_TYPE);
            wrongType.printStackTrace();
            return status;
        }
        status.setOk(true);
        status.setValue(new BasicLiquipItem(id, name, material, lore, enchantments, features, modifiers,
                HashMultimap.create()));
        return status;
    }

    static @NotNull Identifier getIdentifier(@NotNull ItemStack itemStack) {
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
        Result<Identifier, UtilError> identifierResult =
                Identifier.parse(nmsTag.getString("liquip:identifier"), LiquipProvider.DEFAULT_NAMESPACE);
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

    @NotNull Identifier getId();

    @NotNull Component getName();

    @NotNull Material getMaterial();

    @NotNull List<@NotNull Component> getLore();

    @NotNull List<@NotNull LeveledEnchantment> getEnchantments();

    @NotNull List<@NotNull Feature> getFeatures();

    @NotNull List<@NotNull Modifier> getModifiers();

    @NotNull ItemStack newItem();

    <T extends Event> void callEvent(@NotNull Class<@NotNull T> eventClass, @NotNull T event);

    <T extends Event> void registerEvent(@NotNull Class<@NotNull T> eventClass,
                                         @NotNull Consumer<@NotNull T> eventHandler);

    class Builder {
        private final Multimap<Class<? extends Event>, Consumer<? extends Event>> events = HashMultimap.create();
        private Identifier key = null;
        private Component name = null;
        private Material material = null;
        private List<Component> lore = new ArrayList<>();
        private List<LeveledEnchantment> enchantments = new ArrayList<>();
        private List<Feature> features = new ArrayList<>();
        private List<Modifier> modifiers = new ArrayList<>();

        public Builder() {
        }

        public Builder(@NotNull Identifier key, @NotNull String name, @NotNull Material material) {
            this.key = key;
            this.name = MiniMessage.miniMessage().deserialize(name);
            this.material = material;
        }

        public @NotNull Builder identifier(@NotNull Identifier identifier) {
            this.key = identifier;
            return this;
        }

        public @NotNull Builder name(@NotNull String name) {
            this.name = MiniMessage.miniMessage().deserialize(name);
            return this;
        }

        public @NotNull Builder name(@NotNull Component name) {
            this.name = name;
            return this;
        }

        public @NotNull Builder material(@NotNull Material material) {
            this.material = material;
            return this;
        }

        public @NotNull Builder lore(@NotNull List<@NotNull Component> lore) {
            this.lore = lore;
            return this;
        }

        public @NotNull Builder loreLine(@NotNull Component line) {
            lore.add(line);
            return this;
        }

        public @NotNull Builder enchantments(@NotNull List<@NotNull LeveledEnchantment> enchantments) {
            this.enchantments = enchantments;
            return this;
        }

        public @NotNull Builder enchantment(@NotNull LeveledEnchantment enchantment) {
            enchantments.add(enchantment);
            return this;
        }

        public @NotNull Builder features(@NotNull List<@NotNull Feature> features) {
            this.features = features;
            return this;
        }

        public @NotNull Builder feature(@NotNull Feature feature) {
            features.add(feature);
            return this;
        }

        public @NotNull Builder modifiers(@NotNull List<@NotNull Modifier> modifiers) {
            this.modifiers = modifiers;
            return this;
        }

        public @NotNull Builder modifier(@NotNull Modifier modifier) {
            modifiers.add(modifier);
            return this;
        }

        public <T extends Event> @NotNull Builder event(@NotNull Class<@NotNull T> eventClass,
                                                        @NotNull Consumer<@NotNull T> eventConsumer) {
            events.put(eventClass, eventConsumer);
            return this;
        }

        public @Nullable LiquipItem build() {
            if (key == null) {
                return null;
            }
            if (name == null) {
                return null;
            }
            if (material == null) {
                return null;
            }
            return new BasicLiquipItem(key, name, material, lore, enchantments, features, modifiers, events);
        }
    }
}
