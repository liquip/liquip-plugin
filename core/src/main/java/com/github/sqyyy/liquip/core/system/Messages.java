package com.github.sqyyy.liquip.core.system;

public final class Messages {
    public static final String UNEXPECTED_ERROR = "<red>An unexpected error occurred.</red>";
    public static final String ATRRIBUTE_ERROR_STRING = "<red>You have to use letters!</red>";
    public static final String ATTRIBUTE_ERROR_NUMBER = "<red>You have to use a number!</red>";
    public static final String UNKNOWN_COMMAND = """
        <red>That is a unknown command
         Use <bold>/liquip help</bold> for more information.</red>""";
    public static final String PERMISSION_ERROR =
        "<red>You do not have the permission to do that.</red>";
    public static final String ATTRIBUTE_ERROR =
        "<red>You have to use the attributes correctly.</red>";
    public static final String FEATURE_ERROR =
        "<red>This feature is not loaded.</red>";
    public static final String RELOAD_ERROR =
        "<red>Reload failed.</red>";
    public static final String BLOCK_PLACE_ERROR =
        "<red>You can not place this block.</red>";


    //HELP-MESSAGE
    public static final String HELP_MESSAGE = """
        <gradient:aqua:green>LIQUIP HELP</gradient>
        <gray>-----------------------
        For a list of all Commands use <bold>/liquip help commands</bold>

        For a list of all Items use <bold>/liquip help items</bold>

        For a list of all Features use <bold>/liquip help features</bold>

        For a complete Wiki go to <click:OPEN_URL:https://liquip.github.io>our website <blue>(click here)</blue></click>
        """;
    private Messages() {
    }
}
