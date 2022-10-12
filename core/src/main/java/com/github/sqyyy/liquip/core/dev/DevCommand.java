package com.github.sqyyy.liquip.core.dev;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.LiquipProvider;
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
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class DevCommand {
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
        new CommandAPICommand("liquip").executes(DevCommand::liquip).withSubcommands(
                        new CommandAPICommand("craft").withPermission("liquip.command.craft").executesPlayer(DevCommand::craft),
                        new CommandAPICommand("give").withPermission("liquip.command.give").withArguments(
                                new NamespacedKeyArgument("identifier").replaceSuggestions(
                                        ArgumentSuggestions.strings(DevCommand::suggestGive))).executesPlayer(DevCommand::give),
                        new CommandAPICommand("reload").withPermission("liquip.command.reload").executes(DevCommand::reload))
                .register();
    }

    public static void liquip(CommandSender sender, Object[] args) {
        sender.sendMessage(Component.text("--- Liquip help ---").color(TextColor.color(0xC2EFB3)));
        sender.sendMessage(Component.text("/liquip - Show this help").color(TextColor.color(0xC2EFB3)));
        sender.sendMessage(Component.text("/liquip craft - Open a crafting table").color(TextColor.color(0xC2EFB3)));
        sender.sendMessage(
                Component.text("/liquip give <id> - Give yourself a liquip-item").color(TextColor.color(0xC2EFB3)));
        sender.sendMessage(Component.text("/liquip reload - Reload the config").color(TextColor.color(0xC2EFB3)));
    }

    public static void craft(Player player, Object[] args) {
        menu.open(player);
    }

    public static void give(Player sender, Object[] args) {
        final NamespacedKey key = (NamespacedKey) args[0];
        final Identifier identifier = new Identifier(key.getNamespace(), key.getKey());
        final LiquipItem item = Liquip.getProvider().getItemRegistry().get(identifier);
        if (item == null) {
            sender.sendMessage(Component.text("The supplied item could not be found").color(TextColor.color(0xDD1C1A)));
            return;
        }
        sender.getInventory().addItem(item.newItem());
        sender.sendMessage(
                Component.text("Gave [" + identifier + "] to " + sender.getName()).color(TextColor.color(0xC2EFB3)));
    }

    public static void reload(CommandSender sender, Object[] args) {
        sender.sendMessage(Component.text("Reloading config...").color(TextColor.color(0x32A852)));
        Liquip.getProvider().reload();
        sender.sendMessage(Component.text("Done").color(TextColor.color(0x32A852)));
    }

    public static String[] suggestGive(SuggestionInfo info) {
        final String currentArg = info.currentArg();
        final Set<Identifier> keySet = Liquip.getProvider().getItemRegistry().keySet();
        return keySet.stream().filter(it -> it.toString().startsWith(currentArg) ||
                        (it.getNamespace().equals(LiquipProvider.DEFAULT_NAMESPACE) && it.getKey().startsWith(currentArg)))
                .map(it -> it.getNamespace().equals(LiquipProvider.DEFAULT_NAMESPACE) ? it.getKey() : it.toString())
                .toArray(String[]::new);
    }
}
