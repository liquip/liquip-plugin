package io.github.liquip.paper.standalone.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.liquip.api.config.ConfigArray;
import io.github.liquip.api.config.ConfigElement;
import io.github.liquip.api.config.ConfigObject;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class JsonConfigElement implements ConfigElement {
    private final JsonNode node;

    public JsonConfigElement(@NonNull JsonNode node) {
        this.node = node;
    }

    @Override
    public boolean isObject() {
        return this.node.isObject();
    }

    @Override
    public boolean isArray() {
        return this.node.isArray();
    }

    @Override
    public @Nullable ConfigObject asObject() {
        return this.node.isObject() ? new JsonConfigObject((ObjectNode) this.node) : null;
    }

    @Override
    public @Nullable ConfigArray asArray() {
        return this.node.isArray() ? new JsonConfigArray((ArrayNode) this.node) : null;
    }
}
