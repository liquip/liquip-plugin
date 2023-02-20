package io.github.liquip.paper.core.core;

import io.github.liquip.api.event.EventBus;
import io.github.liquip.api.event.EventListener;
import it.unimi.dsi.fastutil.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ArrayListEventBus<T, F> implements EventBus<T, F> {
    protected final List<Pair<EventListener<T>, F>> listeners;

    public ArrayListEventBus() {
        this(16);
    }

    public ArrayListEventBus(int initialCapacity) {
        listeners = new ArrayList<>(initialCapacity);
    }

    @Override
    public void register(@NotNull EventListener<T> listener, @NotNull F filter) {
        listeners.add(Pair.of(listener, filter));
    }

    @Override
    public void unregister(@NotNull EventListener<T> listener) {
        for (int i = 0; i < listeners.size(); i++) {
            if (listeners.get(i)
                .first() == listener) {
                listeners.remove(i);
                return;
            }
        }
    }

    @Override
    public void invoke(@NotNull T instance) {
        for (final Pair<EventListener<T>, F> listener : listeners) {
            final EventListener<T> first = listener.first();
            final F second = listener.second();
            if (shouldInvoke(instance, second)) {
                first.call(instance);
            }
        }
    }

    protected abstract boolean shouldInvoke(@NotNull T instance, @NotNull F filter);
}
