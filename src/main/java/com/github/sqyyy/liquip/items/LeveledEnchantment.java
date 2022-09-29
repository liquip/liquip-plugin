package com.github.sqyyy.liquip.items;

import com.github.sqyyy.liquip.util.Identifier;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.Optional;

public class LeveledEnchantment {
    private final Object enchantment;
    private final int level;

    public LeveledEnchantment(Identifier key, int level) {
        if (key.getKey().equals("minecraft")) {
            enchantment = Enchantment.getByKey(new NamespacedKey(key.getNamespace(), key.getKey()));
        } else {
            throw new RuntimeException("Not implemented");
        }
        this.level = level;
    }

    public Object getEnchantment() {
        return enchantment;
    }

    public boolean isMinecraftEnchantment() {
        return enchantment instanceof Enchantment;
    }

    public boolean isLiquipEnchantment() {
        return enchantment instanceof LiquipEnchantment;
    }

    public Optional<Enchantment> getMinecraftEnchantment() {
        return enchantment instanceof Enchantment enchant ? Optional.of(enchant) : Optional.empty();
    }

    public Optional<LiquipEnchantment> getLiquipEnchantment() {
        return enchantment instanceof LiquipEnchantment enchant ? Optional.of(enchant) : Optional.empty();
    }

    public int getLevel() {
        return level;
    }
}
