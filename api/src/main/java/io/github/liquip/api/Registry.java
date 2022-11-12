package io.github.liquip.api;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Registry<T extends Keyed> extends org.bukkit.Registry<T> {
    void register(@NonNull NamespacedKey key, @NonNull T value);
}
