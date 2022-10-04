package com.github.sqyyy.liquip.core.util;

import com.destroystokyo.paper.Namespaced;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Identifier implements Namespaced {
    private final String namespace;
    private final String key;

    public Identifier(String namespace, String key) {
        Objects.requireNonNull(namespace);
        Objects.requireNonNull(key);
        this.namespace = namespace.toLowerCase();
        this.key = key.toLowerCase();
    }

    public static Result<Identifier, RegistryError> parse(String identifier) {
        Objects.requireNonNull(identifier);
        final var tiles = identifier.split(":");
        if (tiles.length != 2) {
            return Result.err(RegistryError.INVALID_IDENTIFIER);
        }
        return Result.ok(new Identifier(tiles[0].toLowerCase(), tiles[1].toLowerCase()));
    }

    public static Result<Identifier, RegistryError> parse(String identifier, String defaultNamespace) {
        Objects.requireNonNull(identifier);
        final var tiles = identifier.split(":");
        return switch (tiles.length) {
            case 1 -> Result.ok(new Identifier(defaultNamespace.toLowerCase(), tiles[0].toLowerCase()));
            case 2 -> Result.ok(new Identifier(tiles[0].toLowerCase(), tiles[1].toLowerCase()));
            default -> Result.err(RegistryError.INVALID_IDENTIFIER);
        };
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
        final var that = (Identifier) o;
        return namespace.equals(that.namespace) && key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, key);
    }

    @Override
    public String toString() {
        return namespace + ":" + key;
    }
}
