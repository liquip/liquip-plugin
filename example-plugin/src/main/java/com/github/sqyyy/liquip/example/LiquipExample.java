package com.github.sqyyy.liquip.example;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.LiquipProvider;
import com.github.sqyyy.liquip.core.util.Identifier;
import com.github.sqyyy.liquip.example.features.AspectOfTheEnd;
import com.github.sqyyy.liquip.example.features.GrapplingHook;
import org.bukkit.plugin.java.JavaPlugin;

public class LiquipExample extends JavaPlugin {
    private final String namespace = "liquip";

    @Override
    public void onLoad() {
        final LiquipProvider provider = Liquip.getProvider();
        provider.getFeatureRegistry().register(new Identifier(namespace, "grappling_hook"), new GrapplingHook());
        provider.getFeatureRegistry().register(new Identifier(namespace, "aspect_of_the_end"), new AspectOfTheEnd());
    }
}
