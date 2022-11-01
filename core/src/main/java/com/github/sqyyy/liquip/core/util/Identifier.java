package com.github.sqyyy.liquip.core.util;

import com.destroystokyo.paper.Namespaced;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class Identifier implements Namespaced {
    private final String namespace;
    private final String key;

    public Identifier(@NotNull String namespace, @NotNull String key) {
        this.namespace = namespace.toLowerCase();
        this.key = key.toLowerCase();
    }

    public static @NotNull Optional<@NotNull Identifier> parse(@NotNull String identifier) {
        final String[] tiles = identifier.split(":");
        if (tiles.length != 2) {
            return Optional.empty();
        }
        return Optional.of(new Identifier(tiles[0].toLowerCase(), tiles[1].toLowerCase()));
    }

    public static @NotNull Optional<@NotNull Identifier> parse(@NotNull String identifier, @NotNull String defaultNamespace) {
        final String[] tiles = identifier.split(":");
        return switch (tiles.length) {
            case 1 -> Optional.of(new Identifier(defaultNamespace.toLowerCase(), tiles[0].toLowerCase()));
            case 2 -> Optional.of(new Identifier(tiles[0].toLowerCase(), tiles[1].toLowerCase()));
            default -> Optional.empty();
        };
    }

    public static @NotNull Identifier from(@NotNull NamespacedKey namespacedKey) {
        return new Identifier(namespacedKey.getNamespace(), namespacedKey.getKey());
    }

    @Override
    public @NotNull String getNamespace() {
        return namespace;
    }

    @Override
    public @NotNull String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Identifier that = (Identifier) o;
        return namespace.equals(that.namespace) && key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, key);
    }

    @Override
    public @NotNull String toString() {
        return namespace + ":" + key;
    }
}
