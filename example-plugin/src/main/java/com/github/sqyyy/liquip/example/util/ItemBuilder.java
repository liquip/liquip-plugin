package com.github.sqyyy.liquip.example.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {
    private final ItemStack stack;

    public ItemBuilder(Material material) {
        stack = new ItemStack(material);
    }

    public ItemBuilder amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemBuilder itemFlag(ItemFlag flag) {
        stack.addItemFlags(flag);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        stack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder lore(List<Component> lore) {
        stack.lore(lore.stream().map(it -> it.decoration(TextDecoration.ITALIC, false)).toList());
        return this;
    }

    public ItemBuilder displayName(Component displayName) {
        final ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.displayName(displayName.decoration(TextDecoration.ITALIC, false));
        stack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStack __getDirect() {
        return stack;
    }

    public ItemStack build() {
        return stack.clone();
    }
}
