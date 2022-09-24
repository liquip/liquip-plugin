package com.github.sqyyy.liquip;

import com.github.sqyyy.liquip.items.Feature;
import com.github.sqyyy.liquip.util.Registry;
import org.bukkit.plugin.java.JavaPlugin;

public class Liquip extends JavaPlugin {
    public static final String DEFAULT_NAMESPACE = "minecraft";
    private final Registry<Feature> features = new Registry<>();

    @Override
    public void onEnable() {

    }

    public Registry<Feature> getFeatures() {
        return features;
    }
}
