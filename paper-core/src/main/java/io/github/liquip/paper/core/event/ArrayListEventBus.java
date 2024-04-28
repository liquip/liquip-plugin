package io.github.liquip.paper.core.event;

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
        this.listeners = new ArrayList<>(initialCapacity);
    }

    @Override
    public void register(@NotNull EventListener<T> listener, @NotNull F filter) {
        this.listeners.add(Pair.of(listener, filter));
    }

    @Override
    public void unregister(@NotNull EventListener<T> listener) {
        for (int i = 0; i < this.listeners.size(); i++) {
            if (this.listeners.get(i).first() == listener) {
                this.listeners.remove(i);
                return;
            }
        }
    }

    @Override
    public void invoke(@NotNull T instance) {
        for (final Pair<EventListener<T>, F> listener : this.listeners) {
            final EventListener<T> first = listener.first();
            final F second = listener.second();
            if (shouldInvoke(instance, second)) {
                first.call(instance);
            }
        }
    }

    protected abstract boolean shouldInvoke(@NotNull T instance, @NotNull F filter);
}
