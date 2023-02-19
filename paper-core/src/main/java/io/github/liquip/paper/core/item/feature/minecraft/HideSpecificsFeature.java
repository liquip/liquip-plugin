package io.github.liquip.paper.core.item.feature.minecraft;

import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HideSpecificsFeature implements Feature {
    private final NamespacedKey key = new NamespacedKey("minecraft", "hide_specifics");

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public void apply(@NotNull Item item, @NotNull ItemStack itemStack) {
        itemStack.editMeta(it -> it.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS));
    }
}
