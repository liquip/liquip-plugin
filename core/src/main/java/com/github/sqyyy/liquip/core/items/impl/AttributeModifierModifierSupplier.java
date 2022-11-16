package com.github.sqyyy.liquip.core.items.impl;

import com.github.sqyyy.liquip.core.items.Modifier;
import com.github.sqyyy.liquip.core.items.ModifierSupplier;
import com.typesafe.config.Config;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AttributeModifierModifierSupplier implements ModifierSupplier {
    @Override
    public @Nullable Modifier get(@NotNull Config config) {
        if (!config.hasPath("entries")) {
            return null;
        }
        try {
            final List<? extends Config> attributeModifiers = config.getConfigList("entries");
            final List<AttributeModifiersModifier.Entry> entries = new ArrayList<>();
            for (Config attributeModifier : attributeModifiers) {
                if (!attributeModifier.hasPath("attribute") || !attributeModifier.hasPath("name") ||
                    !attributeModifier.hasPath("amount") ||
                    !attributeModifier.hasPath("operation")) {
                    continue;
                }
                final String attributeString = attributeModifier.getString("attribute");
                Attribute attribute = null;
                for (Attribute value : Attribute.values()) {
                    if (value.getKey().equals(NamespacedKey.minecraft(attributeString))) {
                        attribute = value;
                        break;
                    }
                }
                if (attribute == null) {
                    continue;
                }
                final String name = attributeModifier.getString("name");
                double amount = attributeModifier.getDouble("amount");
                final String operationString =
                    attributeModifier.getString("operation").toUpperCase();
                AttributeModifier.Operation operation = null;
                switch (operationString.toLowerCase()) {
                    case "add" -> operation = AttributeModifier.Operation.ADD_NUMBER;
                    case "subtract" -> {
                        amount = -amount;
                        operation = AttributeModifier.Operation.ADD_NUMBER;
                    }
                    case "multiply" -> {
                        operation = AttributeModifier.Operation.MULTIPLY_SCALAR_1;
                    }
                }
                if (operation == null) {
                    continue;
                }
                EquipmentSlot slot = EquipmentSlot.HAND;
                if (attributeModifier.hasPath("slot")) {
                    final String slotString = attributeModifier.getString("slot").toUpperCase();
                    for (EquipmentSlot value : EquipmentSlot.values()) {
                        if (value.toString().equals(slotString)) {
                            slot = value;
                            break;
                        }
                    }
                }
                entries.add(new AttributeModifiersModifier.Entry(attribute,
                    new AttributeModifier(UUID.randomUUID(), name, amount, operation, slot)));
            }
            return new AttributeModifiersModifier(entries);
        } catch (Exception ignored) {
            return null;
        }
    }
}
