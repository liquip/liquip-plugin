package com.github.sqyyy.liquip;

import com.github.sqyyy.liquip.items.Feature;
import com.github.sqyyy.liquip.items.LiquipEnchantment;
import com.github.sqyyy.liquip.items.LiquipItem;
import com.github.sqyyy.liquip.util.BasicRegistry;
import com.github.sqyyy.liquip.util.Registry;

public class LiquipProvider {
    private final Registry<Feature> features;
    private final Registry<LiquipItem> items;
    private final Registry<LiquipEnchantment> enchantments;

    public LiquipProvider() {
        features = new BasicRegistry<>();
        items = new BasicRegistry<>();
        enchantments = new BasicRegistry<>();
    }

    public Registry<Feature> getFeatureRegistry() {
        return features;
    }

    public Registry<LiquipItem> getItemRegistry() {
        return items;
    }

    public Registry<LiquipEnchantment> getEnchantmentRegistry() {
        return enchantments;
    }
}
