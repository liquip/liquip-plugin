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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AttributeModifierFeature implements TaggedFeature<List<Pair<Attribute, AttributeModifier>>> {
    private final NamespacedKey key = new NamespacedKey("minecraft", "attribute_modifier");

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public @Nullable List<Pair<Attribute, AttributeModifier>> initialize(
        @NotNull Item item, @NotNull ConfigElement element
    ) {
        if (element.isObject()) {
            final ConfigObject attributeModifier = element.asObject();
            final Pair<Attribute, AttributeModifier> result = this.parseAttribute(attributeModifier);
            if (result == null) {
                return null;
            }
            return List.of(result);
        }
        if (!element.isArray()) {
            return null;
        }
        final ConfigArray attributeModifiers = element.asArray();
        final List<Pair<Attribute, AttributeModifier>> results = new ArrayList<>();
        for (int i = 0; i < attributeModifiers.size(); i++) {
            if (!attributeModifiers.isObject(i)) {
                return null;
            }
            final ConfigObject attributeModifier = attributeModifiers.getObject(i);
            final Pair<Attribute, AttributeModifier> result = this.parseAttribute(attributeModifier);
            if (result == null) {
                return null;
            }
            results.add(result);
        }
        return results;
    }

    private Pair<Attribute, AttributeModifier> parseAttribute(@NotNull ConfigObject attributeModifier) {
        if (!attributeModifier.hasElement("attribute") || !attributeModifier.isString("attribute")) {
            return null;
        }
        final Attribute attribute;
        try {
            attribute = Attribute.valueOf(attributeModifier.getString("attribute").toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
        if (!attributeModifier.hasElement("name") || !attributeModifier.isString("name")) {
            return null;
        }
        final String name = attributeModifier.getString("name");
        if (!attributeModifier.hasElement("amount") || !attributeModifier.isDouble("amount")) {
            return null;
        }
        final double amount = attributeModifier.getDouble("amount");
        if (!attributeModifier.hasElement("operation") || !attributeModifier.isString("operation")) {
            return null;
        }
        final AttributeModifier.Operation operation = switch (attributeModifier.getString("operation").toLowerCase()) {
            case "+", "add" -> AttributeModifier.Operation.ADD_NUMBER;
            case "*", "multiply" -> AttributeModifier.Operation.MULTIPLY_SCALAR_1;
            default -> null;
        };
        if (operation == null) {
            return null;
        }
        EquipmentSlot slot = null;
        if (attributeModifier.hasElement("slot")) {
            if (!attributeModifier.isString("slot")) {
                return null;
            }
            try {
                slot = EquipmentSlot.valueOf(attributeModifier.getString("slot").toUpperCase());
            } catch (IllegalArgumentException ignored) {
                return null;
            }
        }
        if (slot == null) {
            return Pair.of(attribute, new AttributeModifier(UUID.randomUUID(), name, amount, operation));
        }
        return Pair.of(attribute, new AttributeModifier(UUID.randomUUID(), name, amount, operation, slot));
    }

    @Override
    public void apply(
        @NotNull Item item, @NotNull ItemStack itemStack, @NotNull List<Pair<Attribute, AttributeModifier>> object
    ) {
        itemStack.editMeta(meta -> {
            for (final Pair<Attribute, AttributeModifier> pair : object) {
                meta.addAttributeModifier(pair.key(), pair.value());
            }
        });
    }
}
