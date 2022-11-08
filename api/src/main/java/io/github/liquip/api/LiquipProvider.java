package io.github.liquip.api;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class LiquipProvider {
    private static Liquip instance = null;

    public static @NonNull Liquip get() throws IllegalStateException {
        Liquip instance = LiquipProvider.instance;
        if (instance == null) {
            throw new IllegalStateException("The Liquip API isn't loaded yet");
        }
        return instance;
    }

    @Internal
    static void register(Liquip instance) {
        LiquipProvider.instance =  instance;
    }

    @Internal
    private LiquipProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
