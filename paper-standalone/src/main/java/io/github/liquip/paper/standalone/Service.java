package io.github.liquip.paper.standalone;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public interface Service {
    default void onLoad(@NotNull JavaPlugin plugin) {
    }

    default void onEnable(@NotNull JavaPlugin plugin) {
    }

    default void onReload(@NotNull JavaPlugin plugin) {
    }

    default void onDisable(@NotNull JavaPlugin plugin) {
    }
}
