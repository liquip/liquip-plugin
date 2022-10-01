package com.github.sqyyy.liquip.config;

public enum ConfigError {
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
    ITEMS_REGISTRY_WRONG_TYPE("The items-registry is of wrong type, should be list");

    private final String message;

    ConfigError(String message) {
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
