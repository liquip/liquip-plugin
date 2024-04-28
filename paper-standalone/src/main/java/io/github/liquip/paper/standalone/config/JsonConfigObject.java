package io.github.liquip.paper.standalone.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.liquip.api.config.ConfigArray;
import io.github.liquip.api.config.ConfigObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class JsonConfigObject implements ConfigObject {
    private final ObjectNode node;

    public JsonConfigObject(@NotNull ObjectNode node) {
        Objects.requireNonNull(node);
        this.node = node;
    }

    @Override
    public boolean hasElement(@NotNull String key) {
        Objects.requireNonNull(key);
        return this.node.has(key);
    }

    @Override
    public boolean isBoolean(@NotNull String key) {
        Objects.requireNonNull(key);
        return this.node.get(key).isBoolean();
    }

    @Override
    public boolean isInt(@NotNull String key) {
        Objects.requireNonNull(key);
        return this.node.get(key).isInt();
    }

    @Override
    public boolean isDouble(@NotNull String key) {
        Objects.requireNonNull(key);
        final JsonNode elementNode = this.node.get(key);
        return elementNode.isFloatingPointNumber() || elementNode.isInt();
    }

    @Override
    public boolean isString(@NotNull String key) {
        Objects.requireNonNull(key);
        return this.node.get(key).isTextual();
    }

    @Override
    public boolean isArray(@NotNull String key) {
        Objects.requireNonNull(key);
        return this.node.get(key).isArray();
    }

    @Override
    public boolean isObject(@NotNull String key) {
        Objects.requireNonNull(key);
        return this.node.get(key).isObject();
    }

    @Override
    public boolean getBoolean(@NotNull String key) {
        Objects.requireNonNull(key);
        return this.node.get(key).booleanValue();
    }

    @Override
    public int getInt(@NotNull String key) {
        Objects.requireNonNull(key);
        return this.node.get(key).intValue();
    }

    @Override
    public double getDouble(@NotNull String key) {
        Objects.requireNonNull(key);
        return this.node.get(key).doubleValue();
    }

    @Override
    public @NotNull String getString(@NotNull String key) {
        Objects.requireNonNull(key);
        return this.node.get(key).textValue();
    }

    @Override
    public @NotNull ConfigArray getArray(@NotNull String key) {
        Objects.requireNonNull(key);
        return new JsonConfigArray((ArrayNode) this.node.get(key));
    }

    @Override
    public @NotNull ConfigObject getObject(@NotNull String key) {
        Objects.requireNonNull(key);
        return new JsonConfigObject((ObjectNode) this.node.get(key));
    }
}
