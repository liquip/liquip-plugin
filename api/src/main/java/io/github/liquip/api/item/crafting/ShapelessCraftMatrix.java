package io.github.liquip.api.item.crafting;

public interface ShapelessCraftMatrix extends CraftMatrix {
    @Override
    default boolean isShaped() {
        return false;
    }
}
