package com.github.sqyyy.liquip.core.event;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.util.Identifier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockEventListener implements Listener {
    private final Liquip liquip;

    public BlockEventListener(Liquip liquip) {
        this.liquip = liquip;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        final var handle = Util.checkPlayer(event.getPlayer());

        if (handle == null) {
            return;
        }

        final var identifierString = handle.tag.getString("liquip:identifier");
        final var parsedIdentifier = Identifier.parse(identifierString);

        if (parsedIdentifier.isErr()) {
            liquip.getSLF4JLogger().error("{BlockBreakEvent} Item with invalid identifier");
            return;
        }

        final var identifier = parsedIdentifier.unwrap();
        final var liquipItem = Liquip.getProvider().getItemRegistry().get(identifier);

        if (liquipItem == null) {
            liquip.getSLF4JLogger().error("{BlockBreakEvent} Item with unknown identifier");
            return;
        }

        liquipItem.callEvent(BlockBreakEvent.class, event);
    }
}
