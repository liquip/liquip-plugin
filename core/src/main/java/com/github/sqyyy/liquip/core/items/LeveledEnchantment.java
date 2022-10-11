package com.github.sqyyy.liquip.core.items;

import com.github.sqyyy.liquip.core.system.LiquipError;
import com.github.sqyyy.liquip.core.util.Identifier;
import com.github.sqyyy.liquip.core.util.Registry;
import com.github.sqyyy.liquip.core.util.Status;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class LeveledEnchantment {
    private final Object enchantment;
    private final int level;

    public LeveledEnchantment(@NotNull Enchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public LeveledEnchantment(@NotNull LiquipEnchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public static Status<LeveledEnchantment> parse(@NotNull Identifier key, int level,
                                                   @NotNull Registry<@NotNull LiquipEnchantment> enchantmentRegistry) {
        if (key.getNamespace().equals("minecraft")) {
            final Enchantment enchantmentResult =
                    Enchantment.getByKey(new NamespacedKey(key.getNamespace(), key.getKey()));
            if (enchantmentResult == null) {
                return Status.err(LiquipError.ENCHANTMENT_NOT_FOUND);
            }
            return Status.ok(new LeveledEnchantment(enchantmentResult, level));
        } else {
            if (!enchantmentRegistry.isRegistered(key)) {
                return Status.err(LiquipError.ENCHANTMENT_NOT_FOUND);
            }
            final LiquipEnchantment enchantmentResult = enchantmentRegistry.get(key);
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

    public @NotNull Optional<@NotNull Enchantment> getMinecraftEnchantment() {
        return enchantment instanceof Enchantment enchant ? Optional.of(enchant) : Optional.empty();
    }

    public @NotNull Optional<@NotNull LiquipEnchantment> getLiquipEnchantment() {
        return enchantment instanceof LiquipEnchantment enchant ? Optional.of(enchant) : Optional.empty();
    }

    public int getLevel() {
        return level;
    }

    public void apply(@NotNull ItemStack itemStack) {
        if (enchantment instanceof LiquipEnchantment liquipEnchantment) {
            liquipEnchantment.apply(itemStack, level);
            return;
        }
        if (enchantment instanceof Enchantment bukkitEnchantment) {
            itemStack.addUnsafeEnchantment(bukkitEnchantment, level);
        }
    }
}
