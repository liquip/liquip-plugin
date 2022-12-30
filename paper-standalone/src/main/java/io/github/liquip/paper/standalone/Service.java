package io.github.liquip.paper.standalone;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface Service {
    default void onLoad(@NotNull Plugin plugin) {
    }

    default void onEnable(@NotNull Plugin plugin) {
    }

    default void onReload(@NotNull Plugin plugin) {
    }

    default void onDisable(@NotNull Plugin plugin) {
    }
}
