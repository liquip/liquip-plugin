package com.github.sqyyy.liquip.core.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

class Util {
    private Util() {
    }

    static net.minecraft.world.item.ItemStack checkPlayer(@NotNull Player player) {
        final ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            return null;
        }
        if (!item.hasItemMeta()) {
            return null;
        }
        // TODO - version dependent
        if (!(item instanceof org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack craftItem)) {
            return null;
        }
        final net.minecraft.world.item.ItemStack handle = craftItem.handle;
        if (!handle.hasTag()) {
            return null;
        }
        if (!handle.tag.contains("liquip:identifier")) {
            return null;
        }
        return handle;
    }
}
