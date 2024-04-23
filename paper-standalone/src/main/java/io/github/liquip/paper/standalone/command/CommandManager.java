package io.github.liquip.paper.standalone.command;

import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import io.github.liquip.paper.standalone.Service;
import io.github.liquip.paper.standalone.StandaloneLiquip;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.bukkit.data.MultiplePlayerSelector;
import org.incendo.cloud.bukkit.parser.NamespacedKeyParser;
import org.incendo.cloud.bukkit.parser.selector.MultiplePlayerSelectorParser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.EnumParser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandManager implements Service {
    private final StandaloneLiquip api;
    private final KeySuggestionProvider itemKeySuggestions;

    public CommandManager(@NotNull StandaloneLiquip api) {
        Objects.requireNonNull(api);
        this.api = api;
        this.itemKeySuggestions = new KeySuggestionProvider(List.of());
    }

    private void registerCommand() {
        PaperCommandManager<CommandSender> commandManager = PaperCommandManager.createNative(this.api.getPlugin(),
            ExecutionCoordinator
                .<CommandSender>builder()
                .suggestionsExecutor(ExecutionCoordinator.nonSchedulingExecutor())
                .build());
        commandManager.registerBrigadier();

        commandManager.command(commandManager
            .commandBuilder("liquip", "lq")
            .literal("give")
            .required("target", MultiplePlayerSelectorParser.multiplePlayerSelectorParser())
            .required("key", NamespacedKeyParser.namespacedKeyParser(true), this.itemKeySuggestions)
            .permission("liquip.command.give")
            .handler(this::giveSubcommand));

        commandManager.command(commandManager
            .commandBuilder("liquip", "lq")
            .literal("craft")
            .permission("liquip.command.craft")
            .handler(this::craftSubcommand));

        commandManager.command(commandManager
            .commandBuilder("liquip", "lq")
            .literal("reload")
            .permission("liquip.command.reload")
            .handler(this::reloadSubcommand));

        commandManager.command(commandManager
            .commandBuilder("liquip", "lq")
            .literal("dump")
            .required("type", EnumParser.enumParser(DumpType.class))
            .permission("liquip.command.dump")
            .handler(this::dumpSubcommand));
    }

    private void giveSubcommand(CommandContext<CommandSender> context) {
        CommandSender sender = context.sender();
        final MultiplePlayerSelector targets = context.get("target");
        final NamespacedKey key = context.get("key");
        final Item item = this.api.getItemRegistry().get(key);
        if (item == null) {
            sender.sendMessage(Component.text("Item could not be found.", StandaloneLiquip.COLOR_ERROR));
            return;
        }
        for (Player target : targets.values()) {
            target.getInventory().addItem(item.newItemStack());
            sender.sendMessage(Component.text("Gave [" + key.asString() + "] to " + target.getName() + '.',
                TextColor.color(StandaloneLiquip.COLOR_OK)));
        }
    }

    private void craftSubcommand(CommandContext<CommandSender> context) {
        CommandSender sender = context.sender();
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("You have to be a player.", StandaloneLiquip.COLOR_ERROR));
            return;
        }
        this.api.getCraftingUiManager().openCraftingTable(player);
    }

    private void reloadSubcommand(CommandContext<CommandSender> context) {
        if (this.api.reloadSystem()) {
            context.sender().sendMessage(Component.text("Successfully reloaded config.", StandaloneLiquip.COLOR_OK));
        } else {
            context.sender().sendMessage(Component.text("Could not reload config.", StandaloneLiquip.COLOR_ERROR));
        }
    }

    private void dumpSubcommand(CommandContext<CommandSender> context) {
        DumpType type = context.get("type");
        CommandSender sender = context.sender();
        switch (type) {
            case ITEMS -> {
                for (final Item item : this.api.getItemRegistry()) {
                    sender.sendMessage(Component.text(item.key().asString()));
                }
            }
            case FEATURES -> {
                for (final Feature feature : this.api.getFeatureRegistry()) {
                    sender.sendMessage(Component.text(feature.key().asString()));
                }
            }
            case TAGGED_FEATURES -> {
                for (final TaggedFeature<?> taggedFeature : this.api.getTaggedFeatureRegistry()) {
                    sender.sendMessage(Component.text(taggedFeature.key().asString()));
                }
            }
            case ENCHANTMENTS -> {
                for (final Enchantment enchantment : this.api.getEnchantmentRegistry()) {
                    sender.sendMessage(Component.text(enchantment.key().asString()));
                }
            }
        }
    }

    private void reloadCache() {
        final List<Key> keys = new ArrayList<>();
        for (final Item item : this.api.getItemRegistry()) {
            keys.add(item.key());
        }
        this.itemKeySuggestions.updateKeys(keys);
    }

    @Override
    public void onEnable(@NotNull JavaPlugin plugin) {
        Objects.requireNonNull(plugin);
        this.reloadCache();
        this.registerCommand();
    }

    @Override
    public void onReload(@NotNull JavaPlugin plugin) {
        Objects.requireNonNull(plugin);
        this.reloadCache();
    }
}
