package io.github.liquip.api;

import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import io.github.liquip.api.item.crafting.CraftingSystem;
import io.github.liquip.api.item.crafting.Recipe;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;

/**
 * The Liquip API.
 *
 * <p>The API allows other plugins on the server to interact with Liquip, change behavior of the
 * system and integrate Liquip into other plugins and systems.</p>
 *
 * <p>This interface represents the base of the API. Every other part of the API should be accessed
 * through this interface.</p>
 *
 * <p>To start using the API, you need to obtain an instance of this interface. An instance can be
 * obtained from the static singleton accessor in {@link LiquipProvider}.</p>
 *
 * @since 0.0.1-alpha
 */
public interface Liquip {
    /**
     * Gets the {@link Logger} to be used by system components.
     *
     * @return the system's logger
     * @since 1.2.0-beta
     */
    @NonNull Logger getSystemLogger();

    /**
     * Gets the {@link Registry}, responsible for managing {@link Item} instances.
     *
     * @return the item registry
     * @since 0.0.1-alpha
     */
    @NonNull Registry<Item> getItemRegistry();

    /**
     * Gets the {@link Registry}, responsible for managing {@link Feature} instances.
     *
     * @return the feature registry
     * @since 0.0.1-alpha
     */
    @NonNull Registry<Feature> getFeatureRegistry();

    /**
     * Gets the {@link Registry}, responsible for managing {@link TaggedFeature} instances.
     *
     * @return the tagged feature registry
     * @since 0.0.1-alpha
     */
    @NonNull Registry<TaggedFeature<?>> getTaggedFeatureRegistry();

    /**
     * Gets the {@link Registry}, responsible for managing {@link Enchantment} instances.
     *
     * @return the enchantment registry
     * @since 0.0.1-alpha
     */
    @NonNull Registry<Enchantment> getEnchantmentRegistry();

    /**
     * Return whether the {@link CraftingSystem}, responsible for managing {@link Recipe} instances is available on this instance
     * of the API.
     *
     * @return whether the crafting system is supported by this API
     * @since 0.2.0-alpha
     */
    boolean supportsCraftingSystem();

    /**
     * Gets the {@link CraftingSystem}, responsible for managing {@link Recipe} instances.
     *
     * @return the crafting system
     * @since 0.2.0-alpha
     */
    @NonNull CraftingSystem getCraftingSystem();

    /**
     * Checks if the provided {@link ItemStack} is a custom item.
     *
     * @param itemStack the item that will be checked
     * @return whether the item is custom or not
     * @since 0.0.1-alpha
     */
    boolean isCustomItemStack(@NonNull ItemStack itemStack);

    /**
     * Gets the {@link Key} from the {@link ItemStack}, representing the material of the item.
     *
     * @param itemStack the item that will be used
     * @return the key of the provided item stack
     * @since 0.0.1-alpha
     */
    @NonNull Key getKeyFromItemStack(@NonNull ItemStack itemStack);

    /**
     * Sets the {@link Key} from the {@link ItemStack}, representing the material of the item.
     *
     * @param itemStack the item that will be updated
     * @param key the key that will be set
     * @since 0.2.0-alpha
     */
    void setKeyForItemStack(@NonNull ItemStack itemStack, @NonNull Key key);
}
