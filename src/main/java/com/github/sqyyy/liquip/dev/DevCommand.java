package com.github.sqyyy.liquip.dev;

import com.github.sqyyy.liquip.Liquip;
import com.github.sqyyy.liquip.util.Identifier;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DevCommand implements CommandExecutor {
    private final Liquip liquip;

    public DevCommand(Liquip liquip) {
        this.liquip = liquip;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        MiniMessage mm = MiniMessage.miniMessage();
        if (!(sender instanceof Player)) {
            sender.sendMessage(mm.deserialize("<red>You need to be a player to use that command</red>"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(mm.deserialize("<red>Usage: /liquip give <item></red>"));
            return true;
        }

        final var subcommand = args[0];

        switch (subcommand.toLowerCase()) {
            case "give" -> {
                if (args.length != 2) {
                    sender.sendMessage(mm.deserialize("<red>Too many arguments</red>"));
                    return true;
                }

                final var identifierArg = args[1];
                final var identifierResult = Identifier.parse(identifierArg, Liquip.DEFAULT_NAMESPACE);

                if (identifierResult.isErr()) {
                    sender.sendMessage(mm.deserialize("<red>Invalid identifier</red>"));
                    return true;
                }

                final var identifier = identifierResult.unwrap();
                final var item = liquip.getItemRegistry().get(identifier);

                if (item == null) {
                    sender.sendMessage(mm.deserialize("<red>The supplied item could not be found</red>"));
                    return true;
                }

                ((Player) sender).getInventory().addItem(item.newItem());
            }
            default -> {
                sender.sendMessage(mm.deserialize("<red>Unknown subcommand</red>"));
                return true;
            }
        }

        return true;
    }
}
