package com.github.sqyyy.liquip.example;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.LiquipProvider;
import com.github.sqyyy.liquip.core.items.Feature;
import com.github.sqyyy.liquip.core.util.Identifier;
import com.github.sqyyy.liquip.core.util.Registry;
import com.github.sqyyy.liquip.example.features.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LiquipExample extends JavaPlugin {
    private final String NAMESPACE = "liquip";
    private Map<UUID, PlayerData> playerData = new HashMap<>();
    private GrapplingHook grapplingHook;
    private AspectOfTheEnd aspectOfTheEnd;
    private MagicalWaterBucket magicalWaterBucket;
    private Treecapitator treecapitator;
    private StaffOfPower staffOfPower;

    @Override
    public void onLoad() {
        grapplingHook = new GrapplingHook();
        aspectOfTheEnd = new AspectOfTheEnd(this);
        magicalWaterBucket = new MagicalWaterBucket();
        treecapitator = new Treecapitator(new Treecapitator.Queue(1024, 128));
        staffOfPower = new StaffOfPower(this);
        final LiquipProvider provider = Liquip.getProvider();
        final Registry<Feature> featureRegistry = provider.getFeatureRegistry();
        registerChecked(featureRegistry, new Identifier(NAMESPACE, "grappling_hook"), grapplingHook);
        registerChecked(featureRegistry, new Identifier(NAMESPACE, "aspect_of_the_end"), aspectOfTheEnd);
        registerChecked(featureRegistry, new Identifier(NAMESPACE, "magical_water_bucket"), magicalWaterBucket);
        registerChecked(featureRegistry, new Identifier(NAMESPACE, "treecapitator"), treecapitator);
        registerChecked(featureRegistry, new Identifier(NAMESPACE, "staff_of_power"), staffOfPower);
    }

    private <T> void registerChecked(Registry<T> featureRegistry, Identifier identifier, T entry) {
        if (!featureRegistry.register(identifier, entry)) {
            throw new IllegalStateException(identifier + " already registered");
        }
    }

    @Override
    public void onEnable() {
        treecapitator.start();
        Bukkit.getPluginManager().registerEvents(new ExampleEventHandler(this), this);
    }

    public Map<UUID, PlayerData> getPlayerData() {
        return playerData;
    }
}
