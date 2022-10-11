package com.github.sqyyy.liquip.core.dev;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.core.system.craft.CraftingOutputPane;
import com.github.sqyyy.liquip.core.system.craft.CraftingPane;
import com.github.sqyyy.liquip.core.util.Identifier;
import com.github.sqyyy.liquip.gui.Menu;
import com.github.sqyyy.liquip.gui.MenuType;
import com.github.sqyyy.liquip.gui.Slot;
import com.github.sqyyy.liquip.gui.impl.BasicMenu;
import com.github.sqyyy.liquip.gui.impl.FillItemPane;
import com.github.sqyyy.liquip.gui.impl.FillPane;
import com.github.sqyyy.liquip.gui.impl.StoragePane;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.SuggestionInfo;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.NamespacedKeyArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class DevCommand {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static final Menu menu;

    static {
        Menu.initialize(Liquip.getProvidingPlugin(Liquip.class));
        menu = new BasicMenu(Component.text("Advanced Crafting"), 5, MenuType.CHEST,
                List.of(new FillPane(0, Slot.ROW_ONE_SLOT_ONE, Slot.ROW_FIVE_SLOT_NINE,
                                new ItemStack(Material.BLACK_STAINED_GLASS_PANE)), new CraftingPane(0),
                        new FillPane(1, Slot.ROW_TWO_SLOT_SIX, Slot.ROW_FOUR_SLOT_EIGHT,
                                new ItemStack(Material.LIME_STAINED_GLASS_PANE)),
                        new FillItemPane(1, Slot.ROW_THREE_SLOT_NINE, new ItemStack(Material.KNOWLEDGE_BOOK)),
                        new StoragePane(2, Slot.ROW_TWO_SLOT_TWO, Slot.ROW_FOUR_SLOT_FOUR, (storagePane, inventory) -> {
                        }, (storagePane, inventoryCloseEvent) -> {
                        }), new CraftingOutputPane(2)));
    }

    public DevCommand() {
        new CommandAPICommand("liquip").executes(DevCommand::liquip)
                .withSubcommands(new CommandAPICommand("craft").executesPlayer(DevCommand::craft),
                        new CommandAPICommand("give").withArguments(new NamespacedKeyArgument("id").replaceSuggestions(
                                ArgumentSuggestions.strings(DevCommand::suggestGive))).executesPlayer(DevCommand::give))
                .register();
    }

    public static void liquip(CommandSender sender, Object[] args) {
        sender.sendMessage(Component.text("--- Liquip help ---"));
        sender.sendMessage(Component.text("/liquip - Show this help"));
        sender.sendMessage(Component.text("/liquip craft - Open a crafting table"));
        sender.sendMessage(Component.text("/liquip give <id> - Give yourself a liquip-item"));
    }

    public static void craft(Player player, Object[] args) {
        menu.open(player);
    }

    public static void give(Player sender, Object[] args) {
        final NamespacedKey key = (NamespacedKey) args[0];
        final Identifier identifier = new Identifier(key.getNamespace(), key.getKey());
        final LiquipItem item = Liquip.getProvider().getItemRegistry().get(identifier);
        if (item == null) {
            sender.sendMessage(miniMessage.deserialize("<red>The supplied item could not be found</red>"));
            return;
        }
        sender.getInventory().addItem(item.newItem());
    }

    public static String[] suggestGive(SuggestionInfo info) {
        final Set<Identifier> keySet = Liquip.getProvider().getItemRegistry().keySet();
        return keySet.stream()
                .filter(it -> it.toString().startsWith(info.currentArg()) || it.getKey().startsWith(info.currentArg()))
                .map(Identifier::toString).toArray(String[]::new);
    }
}
