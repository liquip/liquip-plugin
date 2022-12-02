package io.github.liquip.paper.standalone.config;

import io.github.liquip.paper.standalone.StandaloneLiquipImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ConfigLoader {
    private final StandaloneLiquipImpl api;
    private ConfigFile configFile = null;
    private List<ConfigItem> items = null;

    public ConfigLoader(StandaloneLiquipImpl api) {
        this.api = api;
    }

    public boolean loadConfig() {
        final Path path = api.getPlugin().getDataFolder().toPath().resolve("config.json");
        try {
            this.configFile = this.api.getMapper().readValue(path.toFile(), ConfigFile.class);
        } catch (IOException e) {
            this.configFile = null;
            this.api.getPlugin().getSLF4JLogger().trace("Exception whilst loading config file", e);
            return false;
        }
        return true;
    }

    public ConfigFile getConfigFile() {
        return configFile;
    }

    public List<ConfigItem> getItems() {
        return items;
    }
}
