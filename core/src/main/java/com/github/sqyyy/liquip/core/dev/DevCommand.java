package com.github.sqyyy.liquip.core.dev;

import com.github.sqyyy.liquip.core.Liquip;
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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DevCommand implements CommandExecutor {
    private final Menu menu;

    public DevCommand() {
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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        MiniMessage mm = MiniMessage.miniMessage();
        if (!(sender instanceof Player player)) {
            sender.sendMessage(mm.deserialize("<red>You need to be a player to use that command</red>"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(mm.deserialize("<red>Usage: /liquip give <item></red>"));
            return true;
        }

        final var subcommand = args[0];

        switch (subcommand.toLowerCase()) {
            case "give" -> {
                if (args.length != 2) {
                    player.sendMessage(mm.deserialize("<red>Too many arguments</red>"));
                    return true;
                }

                final var identifierArg = args[1];
                final var identifierResult = Identifier.parse(identifierArg, Liquip.DEFAULT_NAMESPACE);

                if (identifierResult.isErr()) {
                    player.sendMessage(mm.deserialize("<red>Invalid identifier</red>"));
                    return true;
                }

                final var identifier = identifierResult.unwrap();
                final var item = Liquip.getProvider().getItemRegistry().get(identifier);

                if (item == null) {
                    player.sendMessage(mm.deserialize("<red>The supplied item could not be found</red>"));
                    return true;
                }

                player.getInventory().addItem(item.newItem());
            }
            case "craft" -> menu.open(player);
            default -> {
                player.sendMessage(mm.deserialize("<red>Unknown subcommand</red>"));
                return true;
            }
        }

        return true;
    }
}
