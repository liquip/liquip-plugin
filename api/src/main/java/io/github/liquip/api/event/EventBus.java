package io.github.liquip.api.event;

import org.jetbrains.annotations.NotNull;

public interface EventBus<T, F> {
    void register(@NotNull EventListener<T> listener, @NotNull F filter);

    void unregister(@NotNull EventListener<T> listener);

    void invoke(@NotNull T instance);
}
