package com.github.sqyyy.liquip.items;

import com.github.sqyyy.liquip.system.LiquipError;
import com.github.sqyyy.liquip.util.Identifier;
import com.github.sqyyy.liquip.util.Registry;
import com.github.sqyyy.liquip.util.Status;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class LeveledEnchantment {
    private final Object enchantment;
    private final int level;

    public LeveledEnchantment(Enchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public LeveledEnchantment(LiquipEnchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public static Status<LeveledEnchantment> parse(Identifier key, int level,
                                                   Registry<LiquipEnchantment> enchantmentRegistry) {
        if (key.getNamespace().equals("minecraft")) {
            final var enchantmentResult = Enchantment.getByKey(new NamespacedKey(key.getNamespace(), key.getKey()));

            if (enchantmentRegistry == null) {
                return Status.err(LiquipError.ENCHANTMENT_NOT_FOUND);
            }

            return Status.ok(new LeveledEnchantment(enchantmentResult, level));
        } else {
            if (!enchantmentRegistry.isRegistered(key)) {
                return Status.err(LiquipError.ENCHANTMENT_NOT_FOUND);
            }

            final var enchantmentResult = enchantmentRegistry.get(key);
            return Status.ok(new LeveledEnchantment(enchantmentResult, level));
        }
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

    public void apply(ItemStack itemStack) {
        if (enchantment instanceof LiquipEnchantment liquipEnchantment) {
            liquipEnchantment.apply(itemStack, level);
            return;
        }
        if (enchantment instanceof Enchantment bukkitEnchantment) {
            itemStack.addUnsafeEnchantment(bukkitEnchantment, level);
        }
    }
}
