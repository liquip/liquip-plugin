package io.github.liquip.paper.core.item.feature.minecraft;

import io.github.liquip.api.config.ConfigElement;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public class CustomModelDataFeature implements TaggedFeature<Integer> {
    private final NamespacedKey key = new NamespacedKey("minecraft", "custom_model_data");

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public @Nullable Integer initialize(@NonNull Item item, @NonNull ConfigElement element) {
        if (!element.isInt()) {
            return null;
        }
        return element.asInt();
    }

    @Override
    public void apply(@NonNull Item item, @NonNull ItemStack itemStack, @NonNull Integer object) {
        itemStack.editMeta(it -> it.setCustomModelData(object));
    }
}
