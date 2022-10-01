package com.github.sqyyy.liquip.items;

public enum LiquipError {
    NO_KEY_FOUND("No key was found"),
    NO_NAME_FOUND("No name was found"),
    NO_MATERIAL_FOUND("No material was found"),
    INVALID_KEY("The key is invalid"),
    INVALID_NAME("The name is invalid"),
    INVALID_MATERIAL("The material is invalid"),
    INVALID_ENCHANTMENT("An enchantment is invalid"),
    INVALID_FEATURE("A feature is invalid"),
    ENCHANTMENT_NOT_FOUND("An enchantment was not found"),
    FEATURE_NOT_FOUND("A feature was not found"),
    WRONG_TYPE("A value has the wrong type");

    private final String message;

    LiquipError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "{" + super.toString() + "}" + message;
    }
}
