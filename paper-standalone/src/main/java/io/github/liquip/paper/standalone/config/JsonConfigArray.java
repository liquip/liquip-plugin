package io.github.liquip.paper.standalone.config;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.liquip.api.config.ConfigArray;
import io.github.liquip.api.config.ConfigObject;
import org.checkerframework.checker.nullness.qual.NonNull;

public class JsonConfigArray implements ConfigArray {
    private final ArrayNode node;

    public JsonConfigArray(ArrayNode node) {
        this.node = node;
    }

    @Override
    public int size() {
        return this.node.size();
    }

    @Override
    public boolean isBoolean(int index) {
        return this.node.get(index).isBoolean();
    }

    @Override
    public boolean isInt(int index) {
        return this.node.get(index).isInt();
    }

    @Override
    public boolean isDouble(int index) {
        return this.node.get(index).isDouble();
    }

    @Override
    public boolean isString(int index) {
        return this.node.get(index).isTextual();
    }

    @Override
    public boolean isArray(int index) {
        return this.node.get(index).isArray();
    }

    @Override
    public boolean isObject(int index) {
        return this.node.get(index).isObject();
    }

    @Override
    public boolean getBoolean(int index) {
        return this.node.get(index).booleanValue();
    }

    @Override
    public int getInt(int index) {
        return this.node.get(index).intValue();
    }

    @Override
    public double getDouble(int index) {
        return this.node.get(index).doubleValue();
    }

    @Override
    public @NonNull String getString(int index) {
        return this.node.get(index).textValue();
    }

    @Override
    public @NonNull ConfigArray getArray(int index) {
        return new JsonConfigArray((ArrayNode) this.node.get(index));
    }

    @Override
    public @NonNull ConfigObject getObject(int index) {
        return new JsonConfigObject((ObjectNode) this.node.get(index));
    }
}
