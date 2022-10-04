package com.github.sqyyy.liquip.core.system;

import com.github.sqyyy.liquip.core.util.Cause;
import com.github.sqyyy.liquip.core.util.Error;
import org.slf4j.Logger;

public enum LiquipError implements Error {
    CONFIG_IO_EXCEPTION("An IO-Exception was thrown"),
    CONFIG_IS_DIRECTORY("The config is not a file"),
    COULD_NOT_REGISTER("The item could not be registered"),
    PLUGIN_DIRECTORY_DOES_NOT_EXIST("The plugin-directory does not exist"),
    NULL_CONFIG("A part of the config is null"),
    NO_ITEMS_REGISTRY("No items-registry was found"),
    ITEM_FILE_NOT_FOUND("The item-file was not found"),
    ITEM_FILE_IS_DIRECTORY("The item-file is not a file"),
    ITEM_INVALID("The item is invalid"),
    ITEM_PATH_INVALID("The path to the item-file is invalid"),
    ITEMS_DIRECTORY_IS_FILE("The items-directory is not a directory"),
    ITEMS_REGISTRY_WRONG_TYPE("The items-registry is of wrong type, should be list"),
    NO_ID_FOUND("No identifier was found"),
    NO_NAME_FOUND("No name was found"),
    NO_MATERIAL_FOUND("No material was found"),
    INVALID_CONFIG("The config is invalid and could not be parsed"),
    INVALID_ID("The identifier is invalid"),
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

    @Override
    public Cause getCause() {
        return null;
    }

    @Override
    public void print(Logger logger, Object... args) {
        logger.error(message);
    }

    @Override
    public String getMessage(Object... args) {
        return message;
    }
}
