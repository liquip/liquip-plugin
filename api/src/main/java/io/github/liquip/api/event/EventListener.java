package io.github.liquip.api.event;

import org.jetbrains.annotations.NotNull;

public interface EventListener<T> {
    void call(@NotNull T instance);
}
