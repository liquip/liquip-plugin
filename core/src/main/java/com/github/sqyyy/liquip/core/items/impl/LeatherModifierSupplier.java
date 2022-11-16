package com.github.sqyyy.liquip.core.items.impl;

import com.github.sqyyy.liquip.core.items.Modifier;
import com.github.sqyyy.liquip.core.items.ModifierSupplier;
import com.typesafe.config.Config;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LeatherModifierSupplier implements ModifierSupplier {
    @Override
    public @Nullable Modifier get(@NotNull Config config) {
        if (!config.hasPath("color")) {
            return null;
        }
        try {
            final int color = config.getInt("color");
            return new LeatherModifier(Color.fromRGB(color & 0xFFFFFF));
        } catch (Exception ignored) {
            return null;
        }
    }
}
