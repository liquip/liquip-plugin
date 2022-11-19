package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.LiquipProvider;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CraftingHashObject {
    private final NamespacedKey[] keys;
    private boolean shaped;

    public CraftingHashObject(@NotNull ItemStack @NotNull [] items, boolean shaped) {
        if (items.length != 9) {
            throw new IllegalArgumentException("items.length != 9");
        }
        keys = new NamespacedKey[items.length];
        for (int i = 0; i < items.length; i++) {
            keys[i] = LiquipProvider.get().getKeyFromItemStack(items[i]);
        }
        this.shaped = shaped;
    }

    public CraftingHashObject(@NotNull NamespacedKey @NotNull [] keys, boolean shaped) {
        if (keys.length != 9) {
            throw new IllegalArgumentException("identifiers.length != 9");
        }
        this.keys = keys;
        this.shaped = shaped;
    }

    public boolean isShaped() {
        return shaped;
    }

    public void setShaped(boolean shaped) {
        this.shaped = shaped;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CraftingHashObject that = (CraftingHashObject) o;
        if (shaped != that.shaped) return false;
        if (shaped) {
            return Arrays.equals(keys, that.keys);
        } else {
            final NamespacedKey[] thisCopy = keys.clone();
            final NamespacedKey[] thatCopy = that.keys.clone();
            Arrays.sort(thisCopy);
            Arrays.sort(thatCopy);
            return Arrays.equals(thisCopy, thatCopy);
        }
    }

    @Override
    public int hashCode() {
        if (shaped) {
            return Arrays.hashCode(keys);
        } else {
            int sum = 0;
            int product = 1;
            for (NamespacedKey identifier : keys) {
                int hashCode = identifier.hashCode();
                sum += hashCode;
                product *= hashCode;
            }
            return sum ^ product;
        }
    }
}
