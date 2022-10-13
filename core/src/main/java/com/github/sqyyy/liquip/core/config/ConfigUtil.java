package com.github.sqyyy.liquip.core.config;

import com.typesafe.config.Config;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ConfigUtil {
    public static @NotNull Optional<@NotNull Integer> getInt(@NotNull Config config, @NotNull String path) {
        try {
            return Optional.of(config.getInt(path));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    public static int getInt(@NotNull Config config, @NotNull String path, int defaultValue) {
        try {
            return config.getInt(path);
        } catch (Exception exception) {
            return defaultValue;
        }
    }

    public static @NotNull Optional<@NotNull Boolean> getBoolean(@NotNull Config config, @NotNull String path) {
        try {
            return Optional.of(config.getBoolean(path));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    public static boolean getBoolean(@NotNull Config config, @NotNull String path, boolean defaultValue) {
        try {
            return config.getBoolean(path);
        } catch (Exception exception) {
            return defaultValue;
        }
    }

    public static @NotNull Optional<@NotNull String> getString(@NotNull Config config, @NotNull String path) {
        try {
            return Optional.of(config.getString(path));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    public static @NotNull Optional<@NotNull List<@NotNull String>> getStringList(@NotNull Config config,
                                                                                  @NotNull String path) {
        try {
            return Optional.of(config.getStringList(path));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    public static @NotNull Optional<@NotNull Config> getConfig(@NotNull Config config, @NotNull String path) {
        try {
            return Optional.of(config.getConfig(path));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    public static @NotNull Optional<@NotNull List<? extends Config>> getConfigList(@NotNull Config config,
                                                                                   @NotNull String path) {
        try {
            return Optional.of(config.getConfigList(path));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
