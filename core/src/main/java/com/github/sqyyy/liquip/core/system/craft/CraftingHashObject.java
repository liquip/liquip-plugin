package com.github.sqyyy.liquip.core.system.craft;

import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.core.util.Identifier;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class CraftingHashObject {
    private final Identifier[] identifiers;
    private boolean shaped;

    public CraftingHashObject(ItemStack[] items, boolean shaped) {
        if (items.length != 9) {
            throw new IllegalArgumentException("items.length != 9");
        }
        identifiers = new Identifier[items.length];
        for (int i = 0; i < items.length; i++) {
            identifiers[i] = LiquipItem.getIdentifier(items[i]);
        }
        this.shaped = shaped;
    }

    public CraftingHashObject(Identifier[] identifiers, boolean shaped) {
        if (identifiers.length != 9) {
            throw new IllegalArgumentException("identifiers.length != 9");
        }
        this.identifiers = identifiers;
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
            return Arrays.equals(identifiers, that.identifiers);
        } else {
            final Identifier[] thisCopy = identifiers.clone();
            final Identifier[] thatCopy = that.identifiers.clone();
            Arrays.sort(thisCopy);
            Arrays.sort(thatCopy);
            return Arrays.equals(thisCopy, thatCopy);
        }
    }

    @Override
    public int hashCode() {
        if (shaped) {
            return Arrays.hashCode(identifiers);
        } else {
            int sum = 0;
            int product = 1;
            for (Identifier identifier : identifiers) {
                int hashCode = identifier.hashCode();
                sum += hashCode;
                product *= hashCode;
            }
            return sum ^ product;
        }
    }
}
