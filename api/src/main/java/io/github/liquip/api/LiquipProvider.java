package io.github.liquip.api;

import org.jetbrains.annotations.NotNull;

/**
 * Provides static access to the {@link Liquip} API.
 */
public final class LiquipProvider {
    private static Liquip instance = null;

    private LiquipProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    /**
     * Gets an instance of the {@link Liquip} API, throwing {@link IllegalStateException} if the API is not loaded yet.
     *
     * <p>This method will never return null.</p>
     *
     * @return an instance of the Liquip API
     * @throws IllegalStateException if the API is not loaded yet
     * @since 0.0.1-alpha
     */
    public static @NotNull Liquip get() throws IllegalStateException {
        Liquip instance = LiquipProvider.instance;
        if (instance == null) {
            throw new IllegalStateException("The Liquip API isn't loaded yet");
        }
        return instance;
    }

    static void register(Liquip instance) {
        LiquipProvider.instance = instance;
    }

    static void unregister() {
        LiquipProvider.instance = null;
    }
}
