package com.github.sqyyy.liquip.core.util;

import java.util.List;

public class Cause {
    private final Error error;
    private final List<Warning> warnings;

    public Cause(Error error, List<Warning> warnings) {
        this.error = error;
        this.warnings = warnings;
    }

    public Cause(Status<?> status) {
        this.error = status.unwrapErr();
        this.warnings = status.getWarnings();
    }

    public Error getError() {
        return error;
    }

    public List<Warning> getWarnings() {
        return warnings;
    }
}
