package com.github.sqyyy.liquip.example;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.LiquipProvider;
import com.github.sqyyy.liquip.core.util.Identifier;
import com.github.sqyyy.liquip.example.features.*;
import org.bukkit.plugin.java.JavaPlugin;

public class LiquipExample extends JavaPlugin {
    private final String NAMESPACE = "liquip";
    private GrapplingHook grapplingHook;
    private AspectOfTheEnd aspectOfTheEnd;
    private MagicalWaterBucket magicalWaterBucket;
    private Treecapitator treecapitator;
    private StaffOfPower staffOfPower;

    @Override
    public void onLoad() {
        grapplingHook = new GrapplingHook();
        aspectOfTheEnd = new AspectOfTheEnd();
        magicalWaterBucket = new MagicalWaterBucket();
        treecapitator = new Treecapitator(new Treecapitator.Queue(1024, 128));
        staffOfPower = new StaffOfPower();
        final LiquipProvider provider = Liquip.getProvider();
        provider.getFeatureRegistry().register(new Identifier(NAMESPACE, "grappling_hook"), grapplingHook);
        provider.getFeatureRegistry().register(new Identifier(NAMESPACE, "aspect_of_the_end"), aspectOfTheEnd);
        provider.getFeatureRegistry().register(new Identifier(NAMESPACE, "magical_water_bucket"), magicalWaterBucket);
        provider.getFeatureRegistry().register(new Identifier(NAMESPACE, "treecapitator"), treecapitator);
        provider.getFeatureRegistry().register(new Identifier(NAMESPACE, "staff_of_power"), staffOfPower);
    }

    @Override
    public void onEnable() {
        treecapitator.start();
    }
}
