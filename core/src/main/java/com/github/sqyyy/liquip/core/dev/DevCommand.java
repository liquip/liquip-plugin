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
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DevCommand {
    private static List<String> suggestions;

    public DevCommand() {
        new CommandAPICommand("liquip").executes(DevCommand::liquip).withSubcommands(
                new CommandAPICommand("craft").withPermission("liquip.command.craft")
                    .executesPlayer(DevCommand::craft),
                new CommandAPICommand("give").withPermission("liquip.command.give").withArguments(
                        new NamespacedKeyArgument("identifier").replaceSuggestions(
                            ArgumentSuggestions.strings(DevCommand::suggestGive)))
                    .executesPlayer(DevCommand::give),
                new CommandAPICommand("reload").withPermission("liquip.command.reload")
                    .executes(DevCommand::reload),
                new CommandAPICommand("loaded-features").withPermission(
                    "liquip.command.loaded.features").executes(DevCommand::features))
            .withPermission("liquip.command").register();
        reloadGiveSuggestions();
    }

    public static void liquip(CommandSender sender, Object[] args) {
        sender.sendMessage(Component.text("--- Liquip help ---").color(TextColor.color(0xC2EFB3)));
        sender.sendMessage(
            Component.text("/liquip - Show this help").color(TextColor.color(0xC2EFB3)));
        sender.sendMessage(Component.text("/liquip craft - Open a crafting table")
            .color(TextColor.color(0xC2EFB3)));
        sender.sendMessage(Component.text("/liquip give <id> - Give yourself a liquip-item")
            .color(TextColor.color(0xC2EFB3)));
        sender.sendMessage(
            Component.text("/liquip reload - Reload the config").color(TextColor.color(0xC2EFB3)));
        sender.sendMessage(Component.text("/liquip features - Print all registered features")
            .color(TextColor.color(0xC2EFB3)));
    }

    public static void craft(Player player, Object[] args) {
        Liquip.getProvider().getCraftingMenu().open(player);
    }

    public static void give(Player sender, Object[] args) {
        final NamespacedKey key = (NamespacedKey) args[0];
        if (key == null) {
            return;
        }
        final Identifier identifier = new Identifier(key.getNamespace(), key.getKey());
        final LiquipItem item = Liquip.getProvider().getItemRegistry().get(identifier);
        if (item == null) {
            sender.sendMessage(Component.text("The supplied item could not be found")
                .color(TextColor.color(0xDD1C1A)));
            return;
        }
        sender.getInventory().addItem(item.newItem());
        sender.sendMessage(Component.text("Gave [" + identifier + "] to " + sender.getName())
            .color(TextColor.color(0xC2EFB3)));
    }

    public static void reload(CommandSender sender, Object[] args) {
        sender.sendMessage(Component.text("Reloading config...").color(TextColor.color(0x32A852)));
        if (!Liquip.getProvider().reload()) {
            sender.sendMessage(
                Component.text("Could not reload successfully").color(TextColor.color(0xDD1C1A)));
            return;
        }
        reloadGiveSuggestions();
        sender.sendMessage(Component.text("Done").color(TextColor.color(0x32A852)));
    }

    public static void features(CommandSender sender, Object[] args) {
        Component message =
            Component.text("--- Features registry ---").color(TextColor.color(0xC2EFB3));
        for (Identifier identifier : Liquip.getProvider().getFeatureRegistry().keySet()) {
            message = message.append(Component.newline());
            message = message.append(Component.text(identifier.toString()))
                .color(TextColor.color(0xC2EFB3));
        }
        message = message.append(Component.newline());
        message = message.append(
            Component.text("--- Features registry ---").color(TextColor.color(0xC2EFB3)));
        sender.sendMessage(message);
    }

    public static String[] suggestGive(SuggestionInfo info) {
        final String currentArg = info.currentArg();
        return suggestions.stream().filter(it -> it.startsWith(currentArg)).toArray(String[]::new);
    }

    public static void reloadGiveSuggestions() {
        final Set<Identifier> keySet = Liquip.getProvider().getItemRegistry().keySet();
        suggestions = new ArrayList<>();
        final Set<String> namespaces = new HashSet<>();
        for (Identifier identifier : keySet) {
            if (!namespaces.contains(identifier.getNamespace())) {
                namespaces.add(identifier.getNamespace());
                suggestions.add(identifier.getNamespace() + ":");
            }
        }
        for (Identifier identifier : keySet) {
            suggestions.add(identifier.toString());
        }
    }
}
