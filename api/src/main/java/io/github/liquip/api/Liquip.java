package io.github.liquip.api;

import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

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
     * Checks if the provided {@link ItemStack} is a custom item.
     *
     * @return whether the item is custom or not
     * @since 0.0.1-alpha
     */
    boolean isCustomItemStack(@NonNull ItemStack itemStack);

    /**
     * Gets the {@link NamespacedKey} from the {@link ItemStack}, representing the material of the
     * item.
     *
     * @return the key of the provided item stack
     * @since 0.0.1-alpha
     */
    @NonNull NamespacedKey getKeyFromItemStack(@NonNull ItemStack itemStack);

    void setKeyForItemStack(@NonNull ItemStack itemStack, @NonNull NamespacedKey key);
}