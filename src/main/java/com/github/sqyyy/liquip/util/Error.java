package com.github.sqyyy.liquip.util;

import org.slf4j.Logger;

import java.util.List;

public interface Error {
    Cause getCause();

    void print(Logger logger, Object... args);

    String getMessage(Object... args);
}
