package io.github.liquip.paper.standalone.item.crafting;

import io.github.liquip.api.Liquip;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO - implement correctly
public class ShapelessCraftingRecipe implements CraftingRecipe {
    private final Liquip api;
    private final List<Integer> counts;
    private final List<NamespacedKey> identifiers;
    private final NamespacedKey resultIdentifier;

    public ShapelessCraftingRecipe(@NonNull Liquip api, @NotNull List<@NotNull Integer> counts,
                                   @NotNull List<@NotNull NamespacedKey> identifiers,
                                   @NotNull NamespacedKey resultIdentifier) {
        this.api = api;
        if (counts.size() != identifiers.size()) {
            throw new IllegalArgumentException("counts.size() != identifiers.size()");
        }
        this.counts = counts;
        this.identifiers = identifiers;
        this.resultIdentifier = resultIdentifier;
    }

    @Override
    public boolean matches(@NotNull ItemStack @NotNull [] grid, @NotNull NamespacedKey @NotNull [] idGrid) {
        if (grid.length != 9) {
            return false;
        }
        if (idGrid.length != 9) {
            return false;
        }
        final Set<Integer> usedIndices = new HashSet<>(9);
        for (int i = 0; i < 9; i++) {
            final NamespacedKey identifier = idGrid[i];
            for (int j = 0; j < identifiers.size(); j++) {
                if (usedIndices.contains(j)) {
                    continue;
                }
                if (!identifiers.get(j).equals(identifier)) {
                    continue;
                }
                if (counts.get(j) < (grid[j].getAmount())) {
                    continue;
                }
                usedIndices.add(j);
            }
        }
        return usedIndices.containsAll(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    }

    @Override
    public @NotNull ItemStack getResult() throws NullPointerException {
        return api.getItemRegistry().get(resultIdentifier).newItemStack();
    }

    @Override
    public void craft(@NotNull ItemStack @NotNull [] grid, @NotNull InventoryClickEvent event) {
        final Inventory topInventory = event.getView().getTopInventory();
        if (event.isShiftClick()) {
            // TODO - implement shift-click support
            for (int row = 0; row < 3; row++) {
                for (int column = 0; column < 3; column++) {
                    final ItemStack item = topInventory.getItem((row + 1) * 9 + column + 1);
                    if (item == null || item.getType() == Material.AIR) {
                        continue;
                    }
                    item.setAmount(item.getAmount() - counts.get(row * 3 + column));
                }
            }
            return;
        }
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                final ItemStack item = topInventory.getItem((row + 1) * 9 + column + 1);
                if (item == null || item.getType() == Material.AIR) {
                    continue;
                }
                item.setAmount(item.getAmount() - counts.get(row * 3 + column));
            }
        }
    }
}
