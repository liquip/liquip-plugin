package com.github.sqyyy.liquip.core.items;

import com.github.sqyyy.liquip.core.LiquipProvider;
import com.github.sqyyy.liquip.core.config.ConfigUtil;
import com.github.sqyyy.liquip.core.items.impl.BasicLiquipItem;
import com.github.sqyyy.liquip.core.system.craft.CraftingHashObject;
import com.github.sqyyy.liquip.core.system.craft.ShapedCraftingRecipe;
import com.github.sqyyy.liquip.core.util.Identifier;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public interface LiquipItem {
    NamespacedKey IDENTIFIER_PDC = new NamespacedKey("liquip", "identifier");

    static @NotNull Optional<@NotNull LiquipItem> fromConfig(
        @NotNull ComponentLogger componentLogger, @NotNull Path path, @NotNull Config config,
        @NotNull LiquipProvider provider) {
        final MiniMessage miniMessage = MiniMessage.miniMessage();
        Identifier id;
        Component name;
        Material material;
        Integer customModelData = null;
        List<Component> lore = List.of();
        List<LeveledEnchantment> enchantments = List.of();
        List<Feature> features = List.of();
        List<Modifier> modifiers = List.of();
        final Optional<Identifier> idResult =
            Identifier.parse(config.getString("id"), LiquipProvider.DEFAULT_NAMESPACE);
        if (idResult.isEmpty()) {
            warn(componentLogger, "Could not parse file for item <aqua>'{}'</aqua>",
                path.toAbsolutePath());
            warn(componentLogger, "  Config contains invalid identifier");
            warn(componentLogger, "  For more information read https://github" +
                ".com/sqyyy-jar/liquip/wiki/Custom-items#identifier");
            return Optional.empty();
        }
        id = idResult.get();
        name = miniMessage.deserialize(config.getString("name"))
            .decoration(TextDecoration.ITALIC, false);
        final NamespacedKey materialKey = NamespacedKey.fromString(config.getString("material"));
        if (materialKey == null) {
            warn(componentLogger, "Could not parse file for item <aqua>'{}'</aqua>", id);
            warn(componentLogger, "  Config contains invalid material");
            warn(componentLogger, "  For more information read https://github" +
                ".com/sqyyy-jar/liquip/wiki/Custom-items#material");
            return Optional.empty();
        }
        material = org.bukkit.Registry.MATERIAL.get(materialKey);
        if (material == null) {
            warn(componentLogger, "Could not parse file for item <aqua>'{}'</aqua>", id);
            warn(componentLogger, "  Config contains unknown material");
            warn(componentLogger, "  For more information read https://github" +
                ".com/sqyyy-jar/liquip/wiki/Custom-items#material");
            return Optional.empty();
        }
        if (config.hasPath("customModelData")) {
            final Optional<Integer> customModelDataResult =
                ConfigUtil.getInt(config, "customModelData");
            if (customModelDataResult.isEmpty()) {
                warn(componentLogger,
                    "Could not parse custom model-data for item <aqua>'{}'</aqua>", id);
                warn(componentLogger, "  Config contains invalid custom model-data");
                warn(componentLogger, "  For more information read https://github" +
                    ".com/sqyyy-jar/liquip/wiki/Custom-items#");
            } else {
                customModelData = customModelDataResult.get();
            }
        }
        lore:
        if (config.hasPath("lore")) {
            final Optional<List<String>> loreResult = ConfigUtil.getStringList(config, "lore");
            if (loreResult.isEmpty()) {
                warn(componentLogger, "Could not parse file for item <aqua>'{}'</aqua>", id);
                warn(componentLogger, "  Config contains invalid lore-tag");
                warn(componentLogger, "  For more information read https://github" +
                    ".com/sqyyy-jar/liquip/wiki/Custom-items#lore");
                break lore;
            }
            final List<String> loreLines = loreResult.get();
            lore = new ArrayList<>();
            for (final String loreLine : loreLines) {
                lore.add(
                    miniMessage.deserialize(loreLine).decoration(TextDecoration.ITALIC, false));
            }
        }
        enchantments:
        if (config.hasPath("enchantments")) {
            final Optional<List<? extends Config>> enchantmentsResult =
                ConfigUtil.getConfigList(config, "enchantments");
            if (enchantmentsResult.isEmpty()) {
                warn(componentLogger, "Could not parse enchantments for item <aqua>'{}'</aqua>",
                    id);
                warn(componentLogger, "  Config contains invalid enchantments-tag");
                warn(componentLogger, "  For more information read https://github" +
                    ".com/sqyyy-jar/liquip/wiki/Custom-items#enchantments");
                break enchantments;
            }
            final List<? extends Config> enchantmentsList = enchantmentsResult.get();
            enchantments = new ArrayList<>();
            for (final Config enchantment : enchantmentsList) {
                if (!enchantment.hasPath("id") || !enchantment.hasPath("level")) {
                    invalidEnchantment(componentLogger, id,
                        "Enchantment does not contain id- and level-tag");
                    continue;
                }
                final Optional<String> enchantmentIdResult =
                    ConfigUtil.getString(enchantment, "id");
                if (enchantmentIdResult.isEmpty()) {
                    invalidEnchantment(componentLogger, id, "Enchantment contains invalid id-tag");
                    continue;
                }
                final String enchantmentIdString = enchantmentIdResult.get();
                final Optional<Integer> enchantmentLevelResult =
                    ConfigUtil.getInt(enchantment, "level");
                if (enchantmentLevelResult.isEmpty()) {
                    invalidEnchantment(componentLogger, id,
                        "Enchantment '" + enchantmentIdString + "' contains invalid level-tag");
                    continue;
                }
                final int enchantmentLevel = enchantmentLevelResult.get();
                final Optional<Identifier> enchantmentId =
                    Identifier.parse(enchantmentIdString, LiquipProvider.DEFAULT_NAMESPACE);
                if (enchantmentId.isEmpty()) {
                    invalidEnchantment(componentLogger, id,
                        "Enchantment '" + enchantmentIdString + "' contains invalid id");
                    continue;
                }
                final Optional<LeveledEnchantment> enchantmentResult =
                    LeveledEnchantment.parse(enchantmentId.get(), enchantmentLevel, provider);
                if (enchantmentResult.isEmpty()) {
                    invalidEnchantment(componentLogger, id,
                        "Enchantment '" + enchantmentId.get() + "' could not be found");
                    continue;
                }
                enchantments.add(enchantmentResult.get());
            }
        }
        features:
        if (config.hasPath("features")) {
            final Optional<List<String>> featuresResult =
                ConfigUtil.getStringList(config, "features");
            if (featuresResult.isEmpty()) {
                warn(componentLogger, "Could not parse features for item <aqua>'{}'</aqua>", id);
                warn(componentLogger, "  Config contains invalid features-tag");
                warn(componentLogger, "  For more information read https://github" +
                    ".com/sqyyy-jar/liquip/wiki/Custom-items#features");
                break features;
            }
            final List<String> featuresList = featuresResult.get();
            features = new ArrayList<>();
            for (final String featureString : featuresList) {
                final Optional<Identifier> featureIdResult =
                    Identifier.parse(featureString, LiquipProvider.DEFAULT_NAMESPACE);
                if (featureIdResult.isEmpty()) {
                    warn(componentLogger, "Could not parse feature for item <aqua>'{}'</aqua>", id);
                    warn(componentLogger, "  Config contains invalid feature '{}'", featureString);
                    warn(componentLogger, "  For more information read https://github" +
                        ".com/sqyyy-jar/liquip/wiki/Custom-items#features");
                    continue;
                }
                final Identifier featureId = featureIdResult.get();
                if (!provider.getFeatureRegistry().isRegistered(featureId)) {
                    warn(componentLogger, "Could not parse feature for item <aqua>'{}'</aqua>", id);
                    warn(componentLogger, "  Config contains unknown feature '{}'", featureId);
                    warn(componentLogger, "  For more information read https://github" +
                        ".com/sqyyy-jar/liquip/wiki/Custom-items#features");
                    continue;
                }
                final Feature feature = provider.getFeatureRegistry().get(featureId);
                features.add(feature);
            }
        }
        modifiers:
        if (config.hasPath("modifiers")) {
            final Optional<Config> modifiersResult = ConfigUtil.getConfig(config, "modifiers");
            if (modifiersResult.isEmpty()) {
                warn(componentLogger, "Could not parse modifiers for item <aqua>'{}'</aqua>", id);
                warn(componentLogger, "  Config contains invalid modifiers-tag");
                warn(componentLogger, "  For more information read https://github" +
                    ".com/sqyyy-jar/liquip/wiki/Custom-items#modifiers");
                break modifiers;
            }
            final Config modifiersMap = modifiersResult.get();
            modifiers = new ArrayList<>();
            final Set<String> modifierIdAccumulator = new HashSet<>();
            for (Map.Entry<String, ConfigValue> modifiersEntry : modifiersMap.entrySet()) {
                final String modifierIdString = modifiersEntry.getKey().split("\\.")[0];
                if (modifierIdAccumulator.contains(modifierIdString)) {
                    continue;
                }
                modifierIdAccumulator.add(modifierIdString);
                final Optional<Identifier> modifierIdResult =
                    Identifier.parse(modifierIdString, LiquipProvider.DEFAULT_NAMESPACE);
                if (modifierIdResult.isEmpty()) {
                    warn(componentLogger,
                        "Could not parse modifier '{}' for item <aqua>'{}'</aqua>",
                        modifierIdString, id);
                    warn(componentLogger, "  Config contains invalid id");
                    warn(componentLogger, "  For more information read https://github" +
                        ".com/sqyyy-jar/liquip/wiki/Custom-items#modifiers");
                    continue;
                }
                final Identifier modifierId = modifierIdResult.get();
                final Optional<Config> modifierResult =
                    ConfigUtil.getConfig(modifiersMap, modifierIdString);
                if (modifierResult.isEmpty()) {
                    warn(componentLogger,
                        "Could not parse modifier '{}' for item <aqua>'{}'</aqua>", modifierId, id);
                    warn(componentLogger, "  Config contains invalid modifier-value");
                    warn(componentLogger, "  For more information read https://github" +
                        ".com/sqyyy-jar/liquip/wiki/Custom-items#modifiers");
                    continue;
                }
                final Config modifierMap = modifierResult.get();
                if (!provider.getModifierRegistry().isRegistered(modifierId)) {
                    warn(componentLogger, "Could find modifier '{}' for item <aqua>'{}'</aqua>",
                        modifierId, id);
                    warn(componentLogger, "  Config contains unknown id");
                    warn(componentLogger, "  For more information read https://github" +
                        ".com/sqyyy-jar/liquip/wiki/Custom-items#modifiers");
                    continue;
                }
                final Modifier modifier =
                    provider.getModifierRegistry().get(modifierId).get(modifierMap);
                if (modifier != null) {
                    modifiers.add(modifier);
                } else {
                    warn(componentLogger,
                        "Could not generate modifier '{}' for item <aqua>'{}'</aqua>", modifierId,
                        id);
                }
            }
        }
        recipes:
        if (config.hasPath("recipes")) {
            final Optional<List<? extends Config>> recipesResult =
                ConfigUtil.getConfigList(config, "recipes");
            if (recipesResult.isEmpty()) {
                warn(componentLogger, "Could not parse recipes for item <aqua>'{}'</aqua>", id);
                warn(componentLogger, "  Config contains invalid recipes-tag");
                warn(componentLogger, "  For more information read https://github" +
                    ".com/sqyyy-jar/liquip/wiki/Custom-items#recipes");
                break recipes;
            }
            final List<? extends Config> recipesList = recipesResult.get();
            recipe:
            for (final Config recipe : recipesList) {
                boolean shapeless = false;
                if (recipe.hasPath("shapeless")) {
                    shapeless = ConfigUtil.getBoolean(recipe, "shapeless", false);
                }
                if (shapeless) {
                    warn(componentLogger,
                        "Shapeless recipes are not implemented yet: <aqua>'{}'</aqua>", id);
                    continue;
                    /*
                    if (!recipe.hasPath("ingredients")) {
                        logger.warn("Could not parse shapeless recipe for item '{}'", id);
                        logger.warn("  Config does not contain ingredients-tag");
                        logger.warn("  For more information read https://github
                        .com/sqyyy-jar/liquip/wiki/Custom-items#recipes");
                        continue;
                    }
                    final Identifier[] ingredientIds = new Identifier[9];
                    final Integer[] ingredientCounts = new Integer[9];
                    Arrays.fill(ingredientCounts, 0);
                    final Optional<List<? extends Config>> ingredientsResult = ConfigUtil
                    .getConfigList(config, "recipes");
                    if (ingredientsResult.isEmpty()) {
                        logger.warn("Could not parse shapeless recipe for item '{}'", id);
                        logger.warn("  Config contains invalid ingredients");
                        logger.warn("  For more information read https://github
                        .com/sqyyy-jar/liquip/wiki/Custom-items#recipes");
                        continue;
                    }
                    final List<? extends Config> ingredientsList = ingredientsResult.get();
                    int index = 0;
                    for (final Config ingredient : ingredientsList) {
                        if (!ingredient.hasPath("id")) {
                            invalidShapelessRecipeIngredient(logger, id, "Config does not contain
                             id-tag");
                            continue recipe;
                        }
                        final Optional<String> ingredientIdResult = ConfigUtil.getString
                        (ingredient, "id");
                        if (ingredientIdResult.isEmpty()) {
                            invalidShapelessRecipeIngredient(logger, id, "Config contains invalid
                             id");
                            continue recipe;
                        }
                        final String ingredientIdString = ingredientIdResult.get();
                        final Optional<Identifier> ingredientId =
                                Identifier.parse(ingredientIdString, LiquipProvider
                                .DEFAULT_NAMESPACE);
                        if (ingredientId.isEmpty()) {
                            invalidShapelessRecipeIngredient(logger, id, "Config contains invalid
                             id");
                            continue recipe;
                        }
                        ingredientCounts[index] = 1;
                        if (ingredient.hasPath("count")) {
                            ingredientCounts[index] = ConfigUtil.getInt(ingredient, "count", 1);
                        }
                        ingredientIds[index] = ingredientId.get();
                        index++;
                        if (index == 9) {
                            break;
                        }
                    }
                    final CraftingHashObject craftingHashObject = new CraftingHashObject
                    (ingredientIds, false);
                    provider.getCraftingRegistry().register(craftingHashObject,
                            new ShapelessCraftingRecipe(Arrays.asList(ingredientCounts), Arrays
                            .asList(ingredientIds), id));
                    continue;
                    */
                }
                if (!recipe.hasPath("shape")) {
                    invalidRecipe(componentLogger, id, "Config does not contain shape-tag");
                    continue;
                }
                if (!recipe.hasPath("placeholders")) {
                    invalidRecipe(componentLogger, id, "Config does not contain placeholders-tag");
                    continue;
                }
                final Optional<List<String>> shapeStringsResult =
                    ConfigUtil.getStringList(recipe, "shape");
                if (shapeStringsResult.isEmpty()) {
                    invalidRecipe(componentLogger, id, "Config contains invalid shape");
                    continue;
                }
                final List<String> shapeStrings = shapeStringsResult.get();
                final Optional<List<? extends Config>> placeholdersResult =
                    ConfigUtil.getConfigList(recipe, "placeholders");
                if (placeholdersResult.isEmpty()) {
                    invalidRecipe(componentLogger, id, "Config has invalid placeholders");
                    continue;
                }
                final List<? extends Config> placeholders = placeholdersResult.get();
                final HashMap<Character, ShapedCraftingRecipe.Placeholder> placeholderMap =
                    new HashMap<>();
                placeholderMap.put(' ',
                    new ShapedCraftingRecipe.Placeholder(' ', new Identifier("minecraft", "air"),
                        0));
                final char[] shape = new char[9];
                for (int i = 0; i < shapeStrings.size(); i++) {
                    final String shapeString = shapeStrings.get(i);
                    if (shapeString.length() < 3) {
                        invalidRecipe(componentLogger, id, "Config contains invalid shape");
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
                    invalidRecipe(componentLogger, id, "Config contains too short shape-tag");
                    continue;
                }
                for (final Config placeholder : placeholders) {
                    if (!placeholder.hasPath("key")) {
                        warn(componentLogger,
                            "Could not parse placeholder for recipe for item <aqua>'{}'</aqua>",
                            id);
                        warn(componentLogger, "  Config does not contain key-tag");
                        warn(componentLogger, "  For more information read https://github" +
                            ".com/sqyyy-jar/liquip/wiki/Custom-items#recipes");
                        continue recipe;
                    }
                    if (!placeholder.hasPath("type")) {
                        warn(componentLogger,
                            "Could not parse placeholder for recipe for item <aqua>'{}'</aqua>",
                            id);
                        warn(componentLogger, "  Config does not contain type-tag");
                        warn(componentLogger, "  For more information read https://github" +
                            ".com/sqyyy-jar/liquip/wiki/Custom-items#recipes");
                        continue recipe;
                    }
                    final String keyString = placeholder.getString("key");
                    if (keyString.isEmpty()) {
                        warn(componentLogger,
                            "Could not parse placeholder for recipe for item <aqua>'{}'</aqua>",
                            id);
                        warn(componentLogger, "  Config contains invalid key");
                        warn(componentLogger, "  For more information read https://github" +
                            ".com/sqyyy-jar/liquip/wiki/Custom-items#recipes");
                        continue recipe;
                    }
                    final char key = keyString.charAt(0);
                    final Optional<Identifier> typeResult =
                        Identifier.parse(placeholder.getString("type"),
                            LiquipProvider.DEFAULT_NAMESPACE);
                    if (typeResult.isEmpty()) {
                        warn(componentLogger,
                            "Could not parse placeholder for recipe for item <aqua>'{}'</aqua>",
                            id);
                        warn(componentLogger, "  Config contains invalid type");
                        warn(componentLogger, "  For more information read https://github" +
                            ".com/sqyyy-jar/liquip/wiki/Custom-items#recipes");
                        continue recipe;
                    }
                    int count = 1;
                    if (placeholder.hasPath("count")) {
                        count = placeholder.getInt("count");
                    }
                    placeholderMap.put(key,
                        new ShapedCraftingRecipe.Placeholder(key, typeResult.get(), count));
                }
                final Identifier[] ingredientIds = new Identifier[9];
                final int[] ingredientCounts = new int[9];
                for (int i = 0; i < shape.length; i++) {
                    if (!placeholderMap.containsKey(shape[i])) {
                        warn(componentLogger, "Could not parse recipe for item <aqua>'{}'</aqua>",
                            id);
                        warn(componentLogger, "  Config does not contain '{}' placeholder",
                            shape[i]);
                        warn(componentLogger, "  For more information read https://github" +
                            ".com/sqyyy-jar/liquip/wiki/Custom-items#recipes");
                        continue recipe;
                    }
                    final ShapedCraftingRecipe.Placeholder placeholder =
                        placeholderMap.get(shape[i]);
                    ingredientIds[i] = placeholder.getType();
                    ingredientCounts[i] = placeholder.getCount();
                }
                final CraftingHashObject hash = new CraftingHashObject(ingredientIds, true);
                provider.getCraftingRegistry()
                    .register(hash, new ShapedCraftingRecipe(ingredientCounts, ingredientIds, id));
            }
        }
        return Optional.of(
            new BasicLiquipItem(id, name, material, customModelData, lore, enchantments, features,
                modifiers, HashMultimap.create()));
    }

    static @NotNull Identifier getIdentifier(@NotNull ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return Identifier.from(itemStack.getType().getKey());
        }
        final String identifierString =
            itemMeta.getPersistentDataContainer().get(IDENTIFIER_PDC, PersistentDataType.STRING);
        if (identifierString == null) {
            return Identifier.from(itemStack.getType().getKey());
        }
        return Identifier.parse(identifierString)
            .orElse(Identifier.from(itemStack.getType().getKey()));
    }

    static @NotNull Optional<Identifier> getCustomIdentifier(@NotNull ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return Optional.empty();
        }
        final String identifierString =
            itemMeta.getPersistentDataContainer().get(IDENTIFIER_PDC, PersistentDataType.STRING);
        if (identifierString == null) {
            return Optional.empty();
        }
        return Identifier.parse(identifierString);
    }

    static @NotNull ItemStack setCustomIdentifier(@NotNull ItemStack itemStack,
        @NotNull Identifier identifier) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            itemMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        }
        itemMeta.getPersistentDataContainer()
            .set(IDENTIFIER_PDC, PersistentDataType.STRING, identifier.toString());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    static boolean hasCustomIdentifier(@NotNull ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return false;
        }
        return itemMeta.getPersistentDataContainer().has(IDENTIFIER_PDC, PersistentDataType.STRING);
    }

    private static void invalidEnchantment(ComponentLogger componentLogger, Identifier id,
        String message) {
        warn(componentLogger, "Could not parse enchantment for item <aqua>'{}'</aqua>", id);
        warn(componentLogger, "  {}", message);
        warn(componentLogger, "  For more information read https://github" +
            ".com/sqyyy-jar/liquip/wiki/Custom-items#enchantments");
    }

    private static void invalidRecipe(ComponentLogger componentLogger, Identifier id,
        String message) {
        warn(componentLogger, "Could not parse recipe for item <aqua>'{}'</aqua>", id);
        warn(componentLogger, "  {}", message);
        warn(componentLogger, "  For more information read https://github" +
            ".com/sqyyy-jar/liquip/wiki/Custom-items#recipes");
    }

    private static void invalidShapelessRecipeIngredient(ComponentLogger componentLogger,
        Identifier id, String message) {
        warn(componentLogger,
            "Could not parse ingredient for shapeless recipe for item <aqua>'{}'</aqua>", id);
        warn(componentLogger, "  {}", message);
        warn(componentLogger, "  For more information read https://github" +
            ".com/sqyyy-jar/liquip/wiki/Custom-items#recipes");
    }

    private static void warn(ComponentLogger componentLogger, String message, Object... args) {
        switch (args.length) {
            case 0 -> componentLogger.warn(MiniMessage.miniMessage().deserialize(message));
            case 1 -> componentLogger.warn(MiniMessage.miniMessage().deserialize(message), args[0]);
            case 2 -> componentLogger.warn(MiniMessage.miniMessage().deserialize(message), args[0],
                args[1]);
            default -> componentLogger.warn(MiniMessage.miniMessage().deserialize(message), args);
        }
    }

    @NotNull Identifier getId();

    @NotNull Component getName();

    @NotNull Material getMaterial();

    boolean hasCustomModelData();

    int getCustomModelData();

    @NotNull List<@NotNull Component> getLore();

    @NotNull List<@NotNull LeveledEnchantment> getEnchantments();

    @NotNull List<@NotNull Feature> getFeatures();

    @NotNull List<@NotNull Modifier> getModifiers();

    @NotNull ItemStack newItem();

    <T extends Event> void callEvent(@NotNull Class<@NotNull T> eventClass, @NotNull T event);

    <T extends Event> void registerEvent(@NotNull Class<@NotNull T> eventClass,
        @NotNull Consumer<@NotNull T> eventHandler);

    class Builder {
        private final Multimap<Class<? extends Event>, Consumer<? extends Event>> events =
            HashMultimap.create();
        private Identifier key = null;
        private Component name = null;
        private Material material = null;
        private Integer customModelData = null;
        private List<Component> lore = new ArrayList<>();
        private List<LeveledEnchantment> enchantments = new ArrayList<>();
        private List<Feature> features = new ArrayList<>();
        private List<Modifier> modifiers = new ArrayList<>();

        public Builder() {
        }

        public Builder(@NotNull Identifier key, @NotNull String name, @NotNull Material material) {
            this.key = key;
            this.name = MiniMessage.miniMessage().deserialize(name)
                .decoration(TextDecoration.ITALIC, false);
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

        public @NotNull Builder customModelData(int customModelData) {
            this.customModelData = customModelData;
            return this;
        }

        public @NotNull Builder customModelData() {
            customModelData = null;
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

        public @NotNull Builder enchantments(
            @NotNull List<@NotNull LeveledEnchantment> enchantments) {
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
            return new BasicLiquipItem(key, name, material, customModelData, lore, enchantments,
                features, modifiers, events);
        }
    }
}
