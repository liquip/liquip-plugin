package io.github.liquip.paper.standalone.item.crafting;

import com.github.sqyyy.jcougar.JCougar;
import com.github.sqyyy.jcougar.Slot;
import com.github.sqyyy.jcougar.Ui;
import com.github.sqyyy.jcougar.impl.UiBuilder;
import com.github.sqyyy.jcougar.impl.panel.SlotClickEventPanel;
import com.github.sqyyy.jcougar.impl.panel.StoragePanel;
import com.github.sqyyy.jcougar.impl.panel.TakeableSlotEventPanel;
import io.github.liquip.api.item.crafting.Recipe;
import io.github.liquip.paper.standalone.StandaloneLiquipImpl;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterators;
import java.util.UUID;
import java.util.stream.StreamSupport;

public class CraftingUiManager {
    private final StandaloneLiquipImpl api;
    private final CraftingTableManager craftingTableManager;
    private final Ui craftingTableUi;
    private final Ui recipeBookUi;
    private final Ui recipeShowcaseUi;
    private final List<Pair<Recipe, ItemStack>> catalogue;
    private final Object2IntMap<UUID> openRecipeBookPages;
    private int pageCount;

    public CraftingUiManager(@NotNull StandaloneLiquipImpl api) {
        this.api = api;
        this.craftingTableManager = new CraftingTableManager(api);
        this.craftingTableUi = this.createCraftingTableUi();
        this.recipeBookUi = this.createRecipeBookUi();
        this.recipeShowcaseUi = this.createRecipeShowcaseUi();
        this.catalogue = new ArrayList<>();
        this.openRecipeBookPages = new Object2IntOpenHashMap<>();
        this.pageCount = 0;
    }

    public void loadCatalogue() {
        final int itemsPerPage = 7 * 4;
        this.catalogue.clear();
        this.catalogue.addAll(StreamSupport.stream(Spliterators.spliteratorUnknownSize(this.api.getCraftingSystem()
                .shapedIterator(), 0), false)
            .map(it -> Pair.of((Recipe) it, it.getShowcaseItem()))
            .toList());
        this.pageCount = this.catalogue.size() % itemsPerPage == 0 ? this.catalogue.size() / itemsPerPage :
            this.catalogue.size() / itemsPerPage + 1;
    }

    public void openCraftingTable(@NotNull Player player) {
        this.craftingTableUi.open(player);
    }

    private Ui createCraftingTableUi() {
        return new UiBuilder.PaperUiBuilder().title(Component.text("Crafting Table"))
            .rows(5)
            .onClose(0, this::craftingTableClose)
            .frame(0, Slot.RowOneSlotOne, Slot.RowFiveSlotNine, this.backgroundFillItem())
            .fill(0, Slot.RowTwoSlotFive, Slot.RowFiveSlotFive, this.backgroundFillItem())
            .addPanel(0,
                new StoragePanel(Slot.RowTwoSlotTwo.chestSlot, Slot.RowFourSlotFour.chestSlot, 9, this::craftingTableUpdate))
            .frame(0, Slot.RowTwoSlotSix, Slot.RowFourSlotEight, this.craftingResultFrameItem())
            .addPanel(0, new TakeableSlotEventPanel(Slot.RowThreeSlotSeven.chestSlot, this::craftingTableTake))
            .put(1, Slot.RowThreeSlotNine, this.recipeBookItem())
            .addPanel(1, new SlotClickEventPanel(Slot.RowThreeSlotNine.chestSlot, this::craftingTableToRecipeBook))
            .build();
    }

    private Ui createRecipeBookUi() {
        return new UiBuilder.PaperUiBuilder().title(Component.text("Recipe Book"))
            .rows(6)
            .frame(0, Slot.RowOneSlotOne, Slot.RowSixSlotNine, this.backgroundFillItem())
            .put(1, Slot.RowThreeSlotNine, this.craftingTableItem())
            .addPanel(1, new SlotClickEventPanel(Slot.RowThreeSlotNine.chestSlot, this::recipeBookToCraftingTable))
            .put(1, Slot.RowSixSlotFour, this.previousArrowItem())
            .addPanel(1, new SlotClickEventPanel(Slot.RowSixSlotFour.chestSlot, this::recipeBookPreviousPage))
            .put(1, Slot.RowSixSlotFive, this.currentPageItem())
            .put(1, Slot.RowSixSlotSix, this.nextArrowItem())
            .addPanel(1, new SlotClickEventPanel(Slot.RowSixSlotSix.chestSlot, this::recipeBookNextPage))
            .onOpen(2, this::recipeBookOpen)
            .onClose(this::recipeBookClose)
            .build();
    }

    private Ui createRecipeShowcaseUi() {
        return new UiBuilder.PaperUiBuilder().title(Component.text("Recipe Showcase"))
            .rows(5)
            .frame(0, Slot.RowOneSlotOne, Slot.RowFiveSlotNine, this.backgroundFillItem())
            .fill(0, Slot.RowTwoSlotFive, Slot.RowFiveSlotFive, this.backgroundFillItem())
            .frame(0, Slot.RowTwoSlotSix, Slot.RowFourSlotEight, this.showcaseResultFrameItem())
            .put(1, Slot.RowThreeSlotNine, this.craftingTableItem())
            .addPanel(1, new SlotClickEventPanel(Slot.RowThreeSlotNine.chestSlot, this::recipeShowcaseToCraftingTable))
            .put(1, Slot.RowFiveSlotNine, this.backArrowItem())
            .addPanel(1, new SlotClickEventPanel(Slot.RowFiveSlotNine.chestSlot, this::recipeShowcaseBack))
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
        this.openRecipeBookPages.put(player.getUniqueId(), 0);
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

    private void craftingTableUpdate(@NotNull Player player, @NotNull InventoryView view) {
        new CraftingScheduler(this.api, view).run();
    }

    private boolean craftingTableTake(@NotNull Player player, @NotNull InventoryView view, int slot) {
        switch (JCougar.UnsafeValues.inventoryClickEvent.getAction()) {
            case DROP_ONE_SLOT, PICKUP_ONE, PICKUP_HALF, PICKUP_SOME -> {
                final ItemStack currentItem = JCougar.UnsafeValues.inventoryClickEvent.getCurrentItem();
                if (currentItem == null || currentItem.getAmount() > 1) {
                    return true;
                }
            }
        }
        return this.craftingTableManager.onTakeItem(player, view, slot);
    }

    public void recipeBookOpen(@NotNull Player player, @NotNull Inventory inventory) {
        this.updateRecipeBook(player, inventory);
    }

    public void recipeBookClose(@NotNull Player player, @NotNull InventoryView view, InventoryCloseEvent.@NotNull Reason reason) {
        if (reason != InventoryCloseEvent.Reason.OPEN_NEW) {
            // TODO openCatalogue.remove(player.uniqueId)
            this.openRecipeBookPages.removeInt(player.getUniqueId());
        }
    }

    private void recipeBookToCraftingTable(@NotNull Player player, @NotNull InventoryView view, int slot) {
        this.craftingTableUi.open(player);
    }

    private void recipeBookToRecipeShowcase(@NotNull Player player, @NotNull InventoryView view, int slot) {
        this.recipeShowcaseUi.open(player);
    }

    private void recipeBookPreviousPage(@NotNull Player player, @NotNull InventoryView view, int slot) {
        final int previousPage = this.openRecipeBookPages.getInt(player.getUniqueId());
        final int newPage = Math.max(previousPage - 1, 0);
        if (previousPage != newPage) {
            this.openRecipeBookPages.put(player.getUniqueId(), newPage);
            this.updateRecipeBook(player, view.getTopInventory());
        }
    }

    private void recipeBookNextPage(@NotNull Player player, @NotNull InventoryView view, int slot) {
        final int previousPage = this.openRecipeBookPages.getInt(player.getUniqueId());
        final int newPage = Math.min(previousPage + 1, this.pageCount - 1);
        if (previousPage != newPage) {
            this.openRecipeBookPages.put(player.getUniqueId(), newPage);
            this.updateRecipeBook(player, view.getTopInventory());
        }
    }

    private void updateRecipeBook(@NotNull Player player, @NotNull Inventory inventory) {
        final int itemsPerPage = 7 * 4;
        for (int row = 1; row <= 4; row++) {
            for (int column = 1; column <= 7; column++) {
                inventory.setItem(row * 9 + column, null);
            }
        }
        final int page = this.openRecipeBookPages.getInt(player.getUniqueId());
        ItemStack item = inventory.getItem(Slot.RowSixSlotFive.chestSlot);
        if (item == null) {
            item = this.currentPageItem();
            inventory.setItem(Slot.RowSixSlotFive.chestSlot, item);
        }
        item.editMeta(it -> it.displayName(Component.text("Page " + (page + 1) + "/" + this.pageCount)
            .decoration(TextDecoration.ITALIC, false)
            .color(TextColor.color(0xFFC0))));
        final int startIndex = page * itemsPerPage;
        final int endIndex = Math.min(startIndex + itemsPerPage, this.catalogue.size());
        for (int i = startIndex; i < endIndex; i++) {
            final int column = Slot.getColumn(7, i) + 1;
            final int row = Slot.getRow(7, i) + 1;
            inventory.setItem(row * 9 + column, this.catalogue.get(i)
                .second());
        }
    }

    private void recipeShowcaseToCraftingTable(@NotNull Player player, @NotNull InventoryView view, int slot) {
        this.craftingTableUi.open(player);
    }

    private void recipeShowcaseBack(@NotNull Player player, @NotNull InventoryView view, int slot) {
        this.recipeBookUi.open(player);
    }
}