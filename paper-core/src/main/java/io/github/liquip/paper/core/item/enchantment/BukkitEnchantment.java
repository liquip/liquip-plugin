package io.github.liquip.paper.core.item.enchantment;

import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Item;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BukkitEnchantment implements Enchantment {
    private final org.bukkit.enchantments.Enchantment base;

    public BukkitEnchantment(org.bukkit.enchantments.Enchantment base) {
        this.base = base;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.base.getKey();
    }

    @Override
    public void apply(@NotNull Item item, @NotNull ItemStack itemStack, int level) {
        itemStack.addUnsafeEnchantment(this.base, level);
    }
}
