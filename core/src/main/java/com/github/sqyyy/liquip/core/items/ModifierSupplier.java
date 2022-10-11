package com.github.sqyyy.liquip.core.items;

import com.typesafe.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ModifierSupplier {
    @Nullable Modifier get(@NotNull Config config);
}
