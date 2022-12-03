package io.github.liquip.paper.standalone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.sqyyy.liquip.gui.Menu;
import com.github.sqyyy.liquip.gui.MenuType;
import com.github.sqyyy.liquip.gui.Slot;
import com.github.sqyyy.liquip.gui.impl.BasicMenu;
import com.github.sqyyy.liquip.gui.impl.FillItemPane;
import com.github.sqyyy.liquip.gui.impl.FillPane;
import com.github.sqyyy.liquip.gui.impl.StoragePane;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPIConfig;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.NamespacedKeyArgument;
import io.github.liquip.api.Liquip;
import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import io.github.liquip.paper.core.util.Registry;
import io.github.liquip.paper.standalone.config.ConfigLoader;
import io.github.liquip.paper.standalone.item.crafting.CraftingOutputPane;
import io.github.liquip.paper.standalone.item.crafting.CraftingPane;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class StandaloneLiquipImpl implements Liquip {
    public static final MiniMessage MM = MiniMessage.miniMessage();
    public static final String NAMESPACE = "liquip";
    private static final NamespacedKey PDC_KEY = new NamespacedKey(NAMESPACE, "key");
    private final Plugin plugin;
    private final ObjectMapper mapper;
    private final ConfigLoader configLoader;
    private final Registry<Item> itemRegistry;
    private final Registry<Feature> featureRegistry;
    private final Registry<TaggedFeature<?>> taggedFeatureRegistry;
    private final Registry<Enchantment> enchantmentRegistry;

    public StandaloneLiquipImpl(Plugin plugin) {
        this.plugin = plugin;
        this.mapper = new JsonMapper().enable(JsonParser.Feature.ALLOW_COMMENTS)
            .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
            .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.configLoader = new ConfigLoader(this);
        this.itemRegistry = new Registry<>();
        this.featureRegistry = new Registry<>();
        this.taggedFeatureRegistry = new Registry<>();
        this.enchantmentRegistry = new Registry<>();
    }

    public static Component MM(String input) {
        return MM.deserialize(input);
    }

    protected void loadSystem() {
        CommandAPI.onLoad(new CommandAPIConfig().silentLogs(true));
        final Menu craftMenu = createCraftMenu();
        final CommandAPICommand liquipCommand =
            new CommandAPICommand("liquip").withPermission("liquip.command");
        final CommandAPICommand liquipGiveCommand =
            new CommandAPICommand("give").withPermission("liquip.command.give")
                .withArguments(new NamespacedKeyArgument("key")).executesPlayer((player, args) -> {
                    final NamespacedKey key = (NamespacedKey) args[0];
                    final Item item = itemRegistry.get(key);
                    if (item == null) {
                        player.sendMessage(
                            Component.text("Item could not be found").color(TextColor.color(0xe32636)));
                        return;
                    }
                    player.getInventory().addItem(item.newItemStack());
                    player.sendMessage(
                        Component.text("Gave [" + key.asString() + "] to " + player.getName())
                            .color(TextColor.color(0x32cd32)));
                });
        final CommandAPICommand liquipCraftCommand =
            new CommandAPICommand("craft").withPermission("liquip.command.craft")
                .executesPlayer((player, args) -> {
                    craftMenu.open(player);
                });
        final CommandAPICommand liquipReloadCommand =
            new CommandAPICommand("reload").withPermission("liquip.command.reload")
                .executes((sender, args) -> {
                    if (reloadSystem()) {
                        sender.sendMessage(Component.text("Successfully reloaded Liquip")
                            .color(TextColor.color(0x32cd32)));
                    } else {
                        sender.sendMessage(Component.text("Could not reload Liquip")
                            .color(TextColor.color(0xe32636)));
                    }
                });
        final CommandAPICommand liquipDumpCommand =
            new CommandAPICommand("dump").withPermission("liquip.command.dump")
                .withArguments(new MultiLiteralArgument("test"/* dump-able things */))
                .executes((sender, args) -> {
                    sender.sendMessage("test");
                });
        liquipCommand.withSubcommands(liquipGiveCommand, liquipCraftCommand, liquipReloadCommand,
            liquipDumpCommand).register();
    }

    protected void enableSystem() {
        CommandAPI.onEnable(plugin);
        Menu.initialize(plugin);
        if (!configLoader.loadConfig()) {
            plugin.getSLF4JLogger().error("Could not load config, disabling...");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    protected boolean reloadSystem() {
        return configLoader.loadConfig();
    }

    protected void disableSystem() {
        CommandAPI.onDisable();
    }

    private Menu createCraftMenu() {
        final ItemStack blackGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        blackGlass.editMeta(meta -> meta.displayName(Component.empty()));
        final ItemStack greenGlass = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        greenGlass.editMeta(meta -> meta.displayName(Component.empty()));
        final ItemStack recipeBook = new ItemStack(Material.KNOWLEDGE_BOOK);
        recipeBook.editMeta(meta -> meta.displayName(
            Component.text("Recipe Book").decoration(TextDecoration.ITALIC, false)));
        return new BasicMenu(Component.text("Advanced Crafting"), 5, MenuType.CHEST,
            List.of(new FillPane(0, Slot.ROW_ONE_SLOT_ONE, Slot.ROW_FIVE_SLOT_NINE, blackGlass),
                new CraftingPane(this, 0),
                new FillPane(1, Slot.ROW_TWO_SLOT_SIX, Slot.ROW_FOUR_SLOT_EIGHT, greenGlass),
                new FillItemPane(1, Slot.ROW_THREE_SLOT_NINE, recipeBook),
                new StoragePane(2, Slot.ROW_TWO_SLOT_TWO, Slot.ROW_FOUR_SLOT_FOUR,
                    (storagePane, inventory) -> {
                    }, (storagePane, inventoryCloseEvent) -> {
                }), new CraftingOutputPane(this, 2)));
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    @Override
    public @NonNull Registry<Item> getItemRegistry() {
        return itemRegistry;
    }

    @Override
    public @NonNull Registry<Feature> getFeatureRegistry() {
        return featureRegistry;
    }

    @Override
    public @NonNull Registry<TaggedFeature<?>> getTaggedFeatureRegistry() {
        return taggedFeatureRegistry;
    }

    @Override
    public @NonNull Registry<Enchantment> getEnchantmentRegistry() {
        return enchantmentRegistry;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public boolean isCustomItemStack(@NonNull ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) {
            return false;
        }
        final PersistentDataContainer persistentDataContainer =
            itemStack.getItemMeta().getPersistentDataContainer();
        if (!persistentDataContainer.has(PDC_KEY, PersistentDataType.STRING)) {
            return false;
        }
        return NamespacedKey.fromString(
            persistentDataContainer.get(PDC_KEY, PersistentDataType.STRING)) != null;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public @NonNull NamespacedKey getKeyFromItemStack(@NonNull ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) {
            return itemStack.getType().getKey();
        }
        final PersistentDataContainer persistentDataContainer =
            itemStack.getItemMeta().getPersistentDataContainer();
        if (!persistentDataContainer.has(PDC_KEY, PersistentDataType.STRING)) {
            return itemStack.getType().getKey();
        }
        final NamespacedKey namespacedKey = NamespacedKey.fromString(
            persistentDataContainer.get(PDC_KEY, PersistentDataType.STRING));
        if (namespacedKey == null) {
            return itemStack.getType().getKey();
        }
        return namespacedKey;
    }

    @Override
    public void setKeyForItemStack(@NonNull ItemStack itemStack, @NonNull Key key) {
        itemStack.editMeta(meta -> meta.getPersistentDataContainer()
            .set(PDC_KEY, PersistentDataType.STRING, key.asString()));
    }
}