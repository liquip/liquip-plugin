package com.github.sqyyy.liquip.core.util;

import org.slf4j.Logger;

public interface Error {
    Cause getCause();

    void print(Logger logger, Object... args);

    String getMessage(Object... args);
}
