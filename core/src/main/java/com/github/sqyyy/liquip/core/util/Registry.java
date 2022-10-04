package com.github.sqyyy.liquip.core.util;

public interface Registry<T> {
    boolean register(Identifier identifier, T value);

    boolean unregister(Identifier identifier);

    boolean isRegistered(Identifier identifier);

    void lock();

    boolean isLocked();

    T get(Identifier identifier);
}
