package com.github.sqyyy.liquip.example;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.LiquipProvider;
import com.github.sqyyy.liquip.core.util.Identifier;
import com.github.sqyyy.liquip.example.features.AspectOfTheEnd;
import com.github.sqyyy.liquip.example.features.GrapplingHook;
import com.github.sqyyy.liquip.example.features.MagicalWaterBucket;
import com.github.sqyyy.liquip.example.features.Treecapitator;
import org.bukkit.plugin.java.JavaPlugin;

public class LiquipExample extends JavaPlugin {
    private final String namespace = "liquip";
    private GrapplingHook grapplingHook;
    private AspectOfTheEnd aspectOfTheEnd;
    private MagicalWaterBucket magicalWaterBucket;
    private Treecapitator treecapitator;

    @Override
    public void onLoad() {
        grapplingHook = new GrapplingHook();
        aspectOfTheEnd = new AspectOfTheEnd();
        magicalWaterBucket = new MagicalWaterBucket();
        treecapitator = new Treecapitator(new Treecapitator.Queue(1024, 128));
        final LiquipProvider provider = Liquip.getProvider();
        provider.getFeatureRegistry().register(new Identifier(namespace, "grappling_hook"), grapplingHook);
        provider.getFeatureRegistry().register(new Identifier(namespace, "aspect_of_the_end"), magicalWaterBucket);
        provider.getFeatureRegistry().register(new Identifier(namespace, "magical_water_bucket"), magicalWaterBucket);
        provider.getFeatureRegistry().register(new Identifier(namespace, "treecapitator"), treecapitator);
    }

    @Override
    public void onEnable() {
        treecapitator.start();
    }
}
