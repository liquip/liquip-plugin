package com.github.sqyyy.liquip.core;

import com.github.sqyyy.liquip.core.config.ConfigLoader;
import com.github.sqyyy.liquip.core.features.MinecraftFeatures;
import com.github.sqyyy.liquip.core.items.Feature;
import com.github.sqyyy.liquip.core.items.LiquipEnchantment;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.core.items.ModifierSupplier;
import com.github.sqyyy.liquip.core.items.impl.AttributeModifierModifierSupplier;
import com.github.sqyyy.liquip.core.items.impl.LeatherModifierSupplier;
import com.github.sqyyy.liquip.core.system.craft.CraftingOutputPane;
import com.github.sqyyy.liquip.core.system.craft.CraftingPane;
import com.github.sqyyy.liquip.core.system.craft.CraftingRegistry;
import com.github.sqyyy.liquip.core.util.Identifier;
import com.github.sqyyy.liquip.core.util.Registry;
import com.github.sqyyy.liquip.core.util.impl.BasicRegistry;
import com.github.sqyyy.liquip.gui.Menu;
import com.github.sqyyy.liquip.gui.MenuType;
import com.github.sqyyy.liquip.gui.Slot;
import com.github.sqyyy.liquip.gui.impl.BasicMenu;
import com.github.sqyyy.liquip.gui.impl.FillItemPane;
import com.github.sqyyy.liquip.gui.impl.FillPane;
import com.github.sqyyy.liquip.gui.impl.StoragePane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LiquipProvider {
    public static final String DEFAULT_NAMESPACE = "minecraft";
    private final Registry<Feature> features;
    private final Registry<ModifierSupplier> modifiers;
    private final Registry<LiquipEnchantment> enchantments;
    private final ConfigLoader configLoader;
    private Menu craftingMenu;
    private Registry<LiquipItem> items;
    private CraftingRegistry recipes;

    public LiquipProvider() {
        items = new BasicRegistry<>();
        features = new BasicRegistry<>();
        modifiers = new BasicRegistry<>();
        enchantments = new BasicRegistry<>();
        recipes = new CraftingRegistry();
        configLoader = new ConfigLoader();
    }

    public @NotNull Registry<@NotNull LiquipItem> getItemRegistry() {
        return items;
    }

    public @NotNull Registry<@NotNull Feature> getFeatureRegistry() {
        return features;
    }

    public Registry<ModifierSupplier> getModifierRegistry() {
        return modifiers;
    }

    public @NotNull Registry<@NotNull LiquipEnchantment> getEnchantmentRegistry() {
        return enchantments;
    }

    public @NotNull CraftingRegistry getCraftingRegistry() {
        return recipes;
    }

    public void registerDefaults() {
        features.register(MinecraftFeatures.UNBREAKABLE, new MinecraftFeatures.Unbreakable());
        features.register(MinecraftFeatures.HIDE_ENCHANTMENTS,
            new MinecraftFeatures.HideEnchantments());
        features.register(MinecraftFeatures.HIDE_UNBREAKABLE,
            new MinecraftFeatures.HideUnbreakable());
        modifiers.register(new Identifier(DEFAULT_NAMESPACE, "attribute_modifier"),
            new AttributeModifierModifierSupplier());
        modifiers.register(new Identifier(DEFAULT_NAMESPACE, "leather_color"),
            new LeatherModifierSupplier());
    }

    void load() {
        Menu.initialize(Liquip.getProvidingPlugin(Liquip.class));
        final ItemStack blackGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        blackGlass.editMeta(meta -> meta.displayName(Component.empty()));
        final ItemStack greenGlass = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        greenGlass.editMeta(meta -> meta.displayName(Component.empty()));
        final ItemStack recipeBook = new ItemStack(Material.KNOWLEDGE_BOOK);
        recipeBook.editMeta(meta -> meta.displayName(
            Component.text("Recipe Book").decoration(TextDecoration.ITALIC, false)));
        craftingMenu = new BasicMenu(Component.text("Advanced Crafting"), 5, MenuType.CHEST,
            List.of(new FillPane(0, Slot.ROW_ONE_SLOT_ONE, Slot.ROW_FIVE_SLOT_NINE, blackGlass),
                new CraftingPane(0),
                new FillPane(1, Slot.ROW_TWO_SLOT_SIX, Slot.ROW_FOUR_SLOT_EIGHT, greenGlass),
                new FillItemPane(1, Slot.ROW_THREE_SLOT_NINE, recipeBook),
                new StoragePane(2, Slot.ROW_TWO_SLOT_TWO, Slot.ROW_FOUR_SLOT_FOUR,
                    (storagePane, inventory) -> {
                    }, (storagePane, inventoryCloseEvent) -> {
                }), new CraftingOutputPane(2)));
        if (configLoader.loadConfig()) {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger()
                .info("Successfully loaded config");
        } else {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger()
                .error("Could not load config successfully");
            Bukkit.getPluginManager().disablePlugin(Liquip.getProvidingPlugin(Liquip.class));
        }
    }

    public boolean reload() {
        items = new BasicRegistry<>();
        recipes = new CraftingRegistry();
        if (configLoader.loadConfig()) {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger()
                .info("Successfully reloaded config");
            return true;
        }
        Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger()
            .error("Could not reload config successfully");
        Bukkit.getPluginManager().disablePlugin(Liquip.getProvidingPlugin(Liquip.class));
        return false;
    }

    public Menu getCraftingMenu() {
        return craftingMenu;
    }
}
