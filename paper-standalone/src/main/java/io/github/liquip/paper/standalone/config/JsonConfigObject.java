package io.github.liquip.paper.standalone.config;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.liquip.api.config.ConfigArray;
import io.github.liquip.api.config.ConfigObject;
import org.checkerframework.checker.nullness.qual.NonNull;

public class JsonConfigObject implements ConfigObject {
    private final ObjectNode node;

    public JsonConfigObject(ObjectNode node) {
        this.node = node;
    }

    @Override
    public boolean hasElement(@NonNull String key) {
        return node.has(key);
    }

    @Override
    public boolean isBoolean(@NonNull String key) {
        return node.get(key).isBoolean();
    }

    @Override
    public boolean isInt(@NonNull String key) {
        return node.get(key).isInt();
    }

    @Override
    public boolean isDouble(@NonNull String key) {
        return node.get(key).isDouble();
    }

    @Override
    public boolean isString(@NonNull String key) {
        return node.get(key).isTextual();
    }

    @Override
    public boolean isArray(@NonNull String key) {
        return node.get(key).isArray();
    }

    @Override
    public boolean isObject(@NonNull String key) {
        return node.get(key).isObject();
    }

    @Override
    public boolean getBoolean(@NonNull String key) {
        return node.get(key).booleanValue();
    }

    @Override
    public int getInt(@NonNull String key) {
        return node.get(key).intValue();
    }

    @Override
    public double getDouble(@NonNull String key) {
        return node.get(key).doubleValue();
    }

    @Override
    public @NonNull String getString(@NonNull String key) {
        return node.get(key).textValue();
    }

    @Override
    public @NonNull ConfigArray getArray(@NonNull String key) {
        return new JsonConfigArray((ArrayNode) node.get(key));
    }

    @Override
    public @NonNull ConfigObject getObject(@NonNull String key) {
        return new JsonConfigObject((ObjectNode) node.get(key));
    }
}
