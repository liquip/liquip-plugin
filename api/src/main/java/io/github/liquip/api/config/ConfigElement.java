package io.github.liquip.api.config;

import org.jetbrains.annotations.NotNull;

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

    @NotNull
    String asString();

    @NotNull
    ConfigArray asArray();

    @NotNull
    ConfigObject asObject();
}
