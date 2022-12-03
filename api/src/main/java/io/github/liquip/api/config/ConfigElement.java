package io.github.liquip.api.config;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface ConfigElement {
    boolean isObject();

    boolean isArray();

    @Nullable ConfigObject asObject();

    @Nullable ConfigArray asArray();
}
