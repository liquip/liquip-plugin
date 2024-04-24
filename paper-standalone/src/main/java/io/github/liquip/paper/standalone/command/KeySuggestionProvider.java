package io.github.liquip.paper.standalone.command;

import net.kyori.adventure.key.Key;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

import java.util.List;

public final class KeySuggestionProvider implements BlockingSuggestionProvider.Strings<CommandSender> {
    private List<String> cache;

    public KeySuggestionProvider(List<Key> keys) {
        this.cache = keys.stream().map(Key::asString).toList();
    }

    public void updateKeys(List<Key> keys) {
        this.cache = keys.stream().map(Key::asString).toList();
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(
        @NonNull CommandContext<CommandSender> commandContext, @NonNull CommandInput input
    ) {
        return this.cache;
    }
}
