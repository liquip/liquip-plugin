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

import java.util.UUID;

public class AttributeModifierModifierSupplier implements ModifierSupplier {
    @Override
    public @Nullable Modifier get(@NotNull Config config) {
        if (!config.hasPath("attribute") || !config.hasPath("name") || !config.hasPath("amount") ||
                !config.hasPath("operation")) {
            return null;
        }
        try {
            final String attributeString = config.getString("attribute");
            Attribute attribute = null;
            for (Attribute value : Attribute.values()) {
                if (value.getKey().equals(NamespacedKey.minecraft(attributeString))) {
                    attribute = value;
                    break;
                }
            }
            if (attributeString == null) {
                return null;
            }
            final String name = config.getString("name");
            double amount = config.getDouble("amount");
            final String operationString = config.getString("operation").toUpperCase();
            AttributeModifier.Operation operation = null;
            switch (operationString.toLowerCase()) {
                case "add" -> operation = AttributeModifier.Operation.ADD_NUMBER;
                case "subtract" -> {
                    amount = -amount;
                    operation = AttributeModifier.Operation.ADD_NUMBER;
                }
                case "multiply" -> {
                    amount -= 1;
                    operation = AttributeModifier.Operation.MULTIPLY_SCALAR_1;
                }
            }
            if (operation == null) {
                return null;
            }
            EquipmentSlot slot = EquipmentSlot.HAND;
            if (config.hasPath("slot")) {
                final String slotString = config.getString("slot").toUpperCase();
                for (EquipmentSlot value : EquipmentSlot.values()) {
                    if (value.toString().equals(slotString)) {
                        slot = value;
                        break;
                    }
                }
            }
            return new AttributeModifierModifier(attribute,
                    new AttributeModifier(UUID.randomUUID(), name, amount, operation, slot));
        } catch (Exception ignored) {
            return null;
        }
    }
}
