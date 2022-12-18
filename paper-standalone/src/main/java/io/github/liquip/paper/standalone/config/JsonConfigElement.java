package io.github.liquip.paper.standalone.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.liquip.api.config.ConfigArray;
import io.github.liquip.api.config.ConfigElement;
import io.github.liquip.api.config.ConfigObject;
import org.checkerframework.checker.nullness.qual.NonNull;

public class JsonConfigElement implements ConfigElement {
    private final JsonNode node;

    public JsonConfigElement(@NonNull JsonNode node) {
        this.node = node;
    }

    @Override
    public boolean isBoolean() {
        return this.node.isBoolean();
    }

    @Override
    public boolean isInt() {
        return this.node.isInt();
    }

    @Override
    public boolean isDouble() {
        return this.node.isDouble();
    }

    @Override
    public boolean isString() {
        return this.node.isTextual();
    }

    @Override
    public boolean isArray() {
        return this.node.isArray();
    }

    @Override
    public boolean isObject() {
        return this.node.isObject();
    }

    @Override
    public boolean asBoolean() {
        return this.node.asBoolean();
    }

    @Override
    public int asInt() {
        return this.node.asInt();
    }

    @Override
    public double asDouble() {
        return this.node.asDouble();
    }

    @Override
    public @NonNull String asString() {
        return this.node.asText();
    }

    @Override
    public @NonNull ConfigArray asArray() {
        return new JsonConfigArray((ArrayNode) this.node);
    }

    @Override
    public @NonNull ConfigObject asObject() {
        return new JsonConfigObject((ObjectNode) this.node);
    }
}
