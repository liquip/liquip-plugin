package io.github.liquip.paper.core.item.feature.minecraft;

import io.github.liquip.api.config.ConfigArray;
import io.github.liquip.api.config.ConfigElement;
import io.github.liquip.api.config.ConfigObject;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AttributeModifierFeature implements TaggedFeature<List<Pair<Attribute, AttributeModifier>>> {
    private final NamespacedKey key = new NamespacedKey("minecraft", "attribute_modifier");

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public @Nullable List<Pair<Attribute, AttributeModifier>> initialize(@NonNull Item item, @NonNull ConfigElement element) {
        if (!element.isArray()) {
            return null;
        }
        final ConfigArray attributeModifiers = element.asArray();
        final List<Pair<Attribute, AttributeModifier>> result = new ArrayList<>();
        for (int i = 0; i < attributeModifiers.size(); i++) {
            if (!attributeModifiers.isObject(i)) {
                return null;
            }
            final ConfigObject attributeModifier = attributeModifiers.getObject(i);
            if (!attributeModifier.hasElement("attribute") || !attributeModifier.isString("attribute")) {
                return null;
            }
            final String attribute = attributeModifier.getString("attribute");
            if (!attributeModifier.hasElement("name") || !attributeModifier.isString("name")) {
                return null;
            }
            final String name = attributeModifier.getString("name");
            if (!attributeModifier.hasElement("amount") || !attributeModifier.isDouble("amount")) {
                return null;
            }
            final double amount = attributeModifier.getDouble("amount");
            result.add(Pair.of(null, new AttributeModifier(name, amount, AttributeModifier.Operation.ADD_NUMBER)));
        }
        return result;
    }

    @Override
    public void apply(@NonNull Item item, @NonNull ItemStack itemStack,
        @NonNull List<Pair<Attribute, AttributeModifier>> object) {
        TaggedFeature.super.apply(item, itemStack, object);
    }
}
