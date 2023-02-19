package io.github.liquip.paper.core.util.api;

import io.github.liquip.api.Liquip;
import io.github.liquip.api.LiquipProvider;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Objects;

public class ApiRegistrationUtil {
    private static final Method REGISTER;
    private static final Method UNREGISTER;

    static {
        try {
            REGISTER = LiquipProvider.class.getDeclaredMethod("register", Liquip.class);
            REGISTER.setAccessible(true);
            UNREGISTER = LiquipProvider.class.getDeclaredMethod("unregister");
            UNREGISTER.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void registerProvider(@NotNull Liquip api) {
        Objects.requireNonNull(api);
        try {
            REGISTER.invoke(null, api);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregisterProvider() {
        try {
            UNREGISTER.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
