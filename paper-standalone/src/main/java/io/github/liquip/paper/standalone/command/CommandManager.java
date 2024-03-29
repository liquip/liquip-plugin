package io.github.liquip.paper.standalone.command;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.NamespacedKeyArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import io.github.liquip.paper.standalone.Service;
import io.github.liquip.paper.standalone.StandaloneLiquip;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandManager implements Service {
    private final StandaloneLiquip api;
    private final List<NamespacedKey> cache;

    public CommandManager(@NotNull StandaloneLiquip api) {
        Objects.requireNonNull(api);
        this.api = api;
        this.cache = new ArrayList<>();
    }

    private @NotNull CommandAPICommand createCommand() {
        return new CommandAPICommand("liquip").withPermission("liquip.command")
            .withSubcommands(this.createGiveSubcommand(), this.createCraftSubcommand(), this.createReloadSubcommand(),
                this.createDumpSubcommand());
    }

    private @NotNull CommandAPICommand createGiveSubcommand() {
        return new CommandAPICommand("give").withPermission("liquip.command.give")
            .withArguments(new NamespacedKeyArgument("key").replaceSuggestions(ArgumentSuggestions.stringCollection(
                suggestionInfo -> this.cache.stream()
                    .map(NamespacedKey::asString)
                    .filter(s -> s.contains(suggestionInfo.currentArg()))
                    .toList())))
            .executesPlayer(this::giveSubcommand);
    }

    private @NotNull CommandAPICommand createCraftSubcommand() {
        return new CommandAPICommand("craft").withPermission("liquip.command.craft")
            .executesPlayer((player, args) -> {
                this.api.getCraftingUiManager()
                    .openCraftingTable(player);
            });
    }

    private @NotNull CommandAPICommand createReloadSubcommand() {
        return new CommandAPICommand("reload").withPermission("liquip.command.reload")
            .executes(this::reloadSubcommand);
    }

    private @NotNull CommandAPICommand createDumpSubcommand() {
        return new CommandAPICommand("dump").withPermission("liquip.command.dump")
            .withArguments(new MultiLiteralArgument("items", "features", "tagged_features", "enchantments"))
            .executes(this::dumpSubcommand);
    }

    private void giveSubcommand(Player player, CommandArguments args) {
        final NamespacedKey key = (NamespacedKey) args.get(0);
        if (key == null) {
            return;
        }
        final Item item = this.api.getItemRegistry()
            .get(key);
        if (item == null) {
            player.sendMessage(Component.text("Item could not be found")
                .color(TextColor.color(StandaloneLiquip.COLOR_ERROR)));
            return;
        }
        player.getInventory()
            .addItem(item.newItemStack());
        player.sendMessage(Component.text("Gave [" + key.asString() + "] to " + player.getName())
            .color(TextColor.color(StandaloneLiquip.COLOR_OK)));
    }

    private void reloadSubcommand(CommandSender sender, CommandArguments args) {
        if (this.api.reloadSystem()) {
            sender.sendMessage(Component.text("Successfully reloaded config")
                .color(TextColor.color(StandaloneLiquip.COLOR_OK)));
        } else {
            sender.sendMessage(Component.text("Could not reload config")
                .color(TextColor.color(StandaloneLiquip.COLOR_ERROR)));
        }
    }

    private void dumpSubcommand(CommandSender sender, CommandArguments args) {
        final String arg = (String) args.get(0);
        if (arg == null) {
            return;
        }
        switch (arg) {
            case "items" -> {
                for (final Item item : this.api.getItemRegistry()) {
                    sender.sendMessage(Component.text(item.key()
                        .asString()));
                }
            }
            case "features" -> {
                for (final Feature feature : this.api.getFeatureRegistry()) {
                    sender.sendMessage(Component.text(feature.key()
                        .asString()));
                }
            }
            case "tagged_features" -> {
                for (final TaggedFeature<?> taggedFeature : this.api.getTaggedFeatureRegistry()) {
                    sender.sendMessage(Component.text(taggedFeature.key()
                        .asString()));
                }
            }
            case "enchantments" -> {
                for (final Enchantment enchantment : this.api.getEnchantmentRegistry()) {
                    sender.sendMessage(Component.text(enchantment.key()
                        .asString()));
                }
            }
        }
    }

    private void reloadCache() {
        this.cache.clear();
        for (final Item item : this.api.getItemRegistry()) {
            this.cache.add(new NamespacedKey(item.key()
                .namespace(), item.key()
                .value()));
        }
    }

    @Override
    public void onLoad(@NotNull JavaPlugin plugin) {
        Objects.requireNonNull(plugin);
        CommandAPI.onLoad(new CommandAPIBukkitConfig(plugin).silentLogs(true));
    }

    @Override
    public void onEnable(@NotNull JavaPlugin plugin) {
        Objects.requireNonNull(plugin);
        CommandAPI.onEnable();
        this.reloadCache();
        this.createCommand()
            .register();
    }

    @Override
    public void onReload(@NotNull JavaPlugin plugin) {
        Objects.requireNonNull(plugin);
        this.reloadCache();
    }

    @Override
    public void onDisable(@NotNull JavaPlugin plugin) {
        Objects.requireNonNull(plugin);
        CommandAPI.onDisable();
    }
}
