package io.github.liquip.api.config;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface ConfigElement {
    boolean isBoolean();

    boolean isInt();

    boolean isDouble();

    boolean isString();

    boolean isArray();

    boolean isObject();

    boolean asBoolean();

    int asInt();

    double asDouble();

    @NonNull String asString();

    @NonNull ConfigArray asArray();

    @NonNull ConfigObject asObject();
}
