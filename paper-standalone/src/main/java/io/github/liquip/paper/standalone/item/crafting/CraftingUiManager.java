package io.github.liquip.paper.standalone.item.crafting;

import com.github.sqyyy.jcougar.Callback;
import com.github.sqyyy.jcougar.Slot;
import com.github.sqyyy.jcougar.Ui;
import com.github.sqyyy.jcougar.impl.UiBuilder;
import com.github.sqyyy.jcougar.impl.panel.SingleSlotClickPanel;
import com.github.sqyyy.jcougar.impl.panel.StoragePanel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CraftingUiManager {
    private final Ui craftingTableUi;
    private final Ui recipeBookUi;
    private final Ui recipeShowcaseUi;

    public CraftingUiManager() {
        this.craftingTableUi = this.createCraftingTableUi();
        this.recipeBookUi = this.createRecipeBookUi();
        this.recipeShowcaseUi = this.createRecipeShowcaseUi();
    }

    public void openCraftingTable(@NotNull Player player) {
        this.craftingTableUi.open(player);
    }

    public void openRecipeBook(@NotNull Player player) {
        this.recipeBookUi.open(player);
    }

    public void openRecipeShowcase(@NotNull Player player) {
        this.recipeShowcaseUi.open(player);
    }

    private Ui createCraftingTableUi() {
        return new UiBuilder.PaperUiBuilder().title(Component.text("Crafting Table"))
            .rows(5)
            .onClose(0, this::craftingTableClose)
            .frame(0, Slot.RowOneSlotOne, Slot.RowFiveSlotNine, this.backgroundFillItem())
            .fill(0, Slot.RowTwoSlotFive, Slot.RowFiveSlotFive, this.backgroundFillItem())
            .addPanel(0, new StoragePanel(Slot.RowTwoSlotTwo.chestSlot, Slot.RowFourSlotFour.chestSlot, 9, Callback.Open.EMPTY,
                Callback.Close.EMPTY, Callback.Update.EMPTY))
            .frame(0, Slot.RowTwoSlotSix, Slot.RowFourSlotEight, this.craftingResultFrameItem())
            .put(1, Slot.RowThreeSlotNine, this.recipeBookItem())
            .addPanel(1, new SingleSlotClickPanel(Slot.RowThreeSlotNine.chestSlot, this::craftingTableToRecipeBook))
            .build();
    }

    private Ui createRecipeBookUi() {
        return new UiBuilder.PaperUiBuilder().title(Component.text("Recipe Book"))
            .rows(6)
            .frame(0, Slot.RowOneSlotOne, Slot.RowSixSlotNine, this.backgroundFillItem())
            .put(1, Slot.RowThreeSlotNine, this.craftingTableItem())
            .addPanel(1, new SingleSlotClickPanel(Slot.RowThreeSlotNine.chestSlot, this::recipeBookToCraftingTable))
            .put(1, Slot.RowSixSlotFour, this.previousArrowItem())
            .addPanel(1, new SingleSlotClickPanel(Slot.RowSixSlotFour.chestSlot, this::recipeBookPreviousPage))
            .put(1, Slot.RowSixSlotFive, this.currentPageItem())
            .put(1, Slot.RowSixSlotSix, this.nextArrowItem())
            .addPanel(1, new SingleSlotClickPanel(Slot.RowSixSlotSix.chestSlot, this::recipeBookNextPage))
            .build();
    }

    private Ui createRecipeShowcaseUi() {
        return new UiBuilder.PaperUiBuilder().title(Component.text("Recipe Showcase"))
            .rows(5)
            .frame(0, Slot.RowOneSlotOne, Slot.RowFiveSlotNine, this.backgroundFillItem())
            .fill(0, Slot.RowTwoSlotFive, Slot.RowFiveSlotFive, this.backgroundFillItem())
            .frame(0, Slot.RowTwoSlotSix, Slot.RowFourSlotEight, this.showcaseResultFrameItem())
            .put(1, Slot.RowThreeSlotNine, this.craftingTableItem())
            .addPanel(1, new SingleSlotClickPanel(Slot.RowThreeSlotNine.chestSlot, this::recipeShowcaseToCraftingTable))
            .put(1, Slot.RowFiveSlotNine, this.backArrowItem())
            .addPanel(1, new SingleSlotClickPanel(Slot.RowFiveSlotNine.chestSlot, this::recipeShowcaseBack))
            .build();
    }

    private ItemStack backgroundFillItem() {
        final ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        item.editMeta(it -> it.displayName(Component.empty()));
        return item;
    }

    private ItemStack craftingResultFrameItem() {
        final ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        item.editMeta(it -> it.displayName(Component.empty()));
        return item;
    }

    private ItemStack showcaseResultFrameItem() {
        final ItemStack item = new ItemStack(Material.BARRIER);
        item.editMeta(it -> it.displayName(Component.empty()));
        return item;
    }

    private ItemStack recipeBookItem() {
        final ItemStack item = new ItemStack(Material.KNOWLEDGE_BOOK);
        item.editMeta(it -> it.displayName(Component.empty()
            .decoration(TextDecoration.ITALIC, false)
            .color(TextColor.color(0xFFC0))
            .append(Component.text("Recipe Book"))));
        return item;
    }

    private ItemStack craftingTableItem() {
        final ItemStack item = new ItemStack(Material.CRAFTING_TABLE);
        item.editMeta(it -> it.displayName(Component.empty()
            .decoration(TextDecoration.ITALIC, false)
            .color(TextColor.color(0xFFC0))
            .append(Component.text("Crafting Table"))));
        return item;
    }

    private ItemStack previousArrowItem() {
        final ItemStack item = new ItemStack(Material.ARROW);
        item.editMeta(it -> it.displayName(Component.empty()
            .decoration(TextDecoration.ITALIC, false)
            .color(TextColor.color(0xFFC0))
            .append(Component.text("Previous Page"))));
        return item;
    }

    private ItemStack nextArrowItem() {
        final ItemStack item = new ItemStack(Material.ARROW);
        item.editMeta(it -> it.displayName(Component.empty()
            .decoration(TextDecoration.ITALIC, false)
            .color(TextColor.color(0xFFC0))
            .append(Component.text("Next Page"))));
        return item;
    }

    private ItemStack currentPageItem() {
        final ItemStack item = new ItemStack(Material.PAPER);
        item.editMeta(it -> it.displayName(Component.empty()
            .decoration(TextDecoration.ITALIC, false)
            .color(TextColor.color(0xFFC0))
            .append(Component.text("Page 0/0"))));
        return item;
    }

    private ItemStack backArrowItem() {
        final ItemStack item = new ItemStack(Material.ARROW);
        item.editMeta(it -> it.displayName(Component.empty()
            .decoration(TextDecoration.ITALIC, false)
            .color(TextColor.color(0xFFC0))
            .append(Component.text("Back"))));
        return item;
    }

    private void craftingTableToRecipeBook(@NotNull Player player, @NotNull InventoryView view, int slot) {
        this.recipeBookUi.open(player);
    }

    private void craftingTableClose(@NotNull Player player, @NotNull InventoryView view,
        InventoryCloseEvent.@NotNull Reason reason) {
        final Inventory topInventory = view.getTopInventory();
        final Inventory bottomInventory = view.getBottomInventory();
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                final int slot = (row + 1) * 9 + column + 1;
                final ItemStack item = topInventory.getItem(slot);
                if (item == null || item.getType() == Material.AIR) {
                    continue;
                }
                if (bottomInventory.firstEmpty() != -1) {
                    bottomInventory.addItem(item);
                    continue;
                }
                player.getWorld()
                    .dropItem(player.getEyeLocation(), item);
            }
        }
    }

    private void recipeBookToCraftingTable(@NotNull Player player, @NotNull InventoryView view, int slot) {
        this.craftingTableUi.open(player);
    }

    private void recipeBookToRecipeShowcase(@NotNull Player player, @NotNull InventoryView view, int slot) {
        this.recipeShowcaseUi.open(player);
    }

    private void recipeBookPreviousPage(@NotNull Player player, @NotNull InventoryView view, int slot) {
        player.sendMessage("Previous");
    }

    private void recipeBookNextPage(@NotNull Player player, @NotNull InventoryView view, int slot) {
        player.sendMessage("Next");
    }

    private void recipeShowcaseToCraftingTable(@NotNull Player player, @NotNull InventoryView view, int slot) {
        this.craftingTableUi.open(player);
    }

    private void recipeShowcaseBack(@NotNull Player player, @NotNull InventoryView view, int slot) {
        this.recipeBookUi.open(player);
    }
}
