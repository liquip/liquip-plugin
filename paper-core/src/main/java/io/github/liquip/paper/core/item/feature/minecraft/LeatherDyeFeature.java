package io.github.liquip.paper.core.item.feature.minecraft;

import io.github.liquip.api.config.ConfigElement;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public class LeatherDyeFeature implements TaggedFeature<Integer> {
    private final NamespacedKey key = new NamespacedKey("minecraft", "dye_leather");

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public @Nullable Integer initialize(@NonNull Item item, @NonNull ConfigElement element) {
        if (element.isInt()) {
            return element.asInt();
        }
        if (!element.isString()) {
            return null;
        }
        final String s = element.asString();
        try {
            return Integer.parseInt(s, 1, s.length(), 16);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public void apply(@NonNull Item item, @NonNull ItemStack itemStack, @NonNull Integer object) {
        itemStack.editMeta(it -> {
            if (it instanceof LeatherArmorMeta) {
                ((LeatherArmorMeta) it).setColor(Color.fromRGB(object));
            }
        });
    }
}
