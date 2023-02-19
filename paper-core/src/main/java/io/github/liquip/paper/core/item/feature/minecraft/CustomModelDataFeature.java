package io.github.liquip.paper.core.item.feature.minecraft;

import io.github.liquip.api.config.ConfigElement;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomModelDataFeature implements TaggedFeature<Integer> {
    private final NamespacedKey key = new NamespacedKey("minecraft", "custom_model_data");

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public @Nullable Integer initialize(@NotNull Item item, @NotNull ConfigElement element) {
        if (!element.isInt()) {
            return null;
        }
        return element.asInt();
    }

    @Override
    public void apply(@NotNull Item item, @NotNull ItemStack itemStack, @NotNull Integer object) {
        itemStack.editMeta(it -> it.setCustomModelData(object));
    }
}
