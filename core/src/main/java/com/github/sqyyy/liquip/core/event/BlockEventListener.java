package com.github.sqyyy.liquip.core.event;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.core.util.Identifier;
import com.github.sqyyy.liquip.core.util.Result;
import com.github.sqyyy.liquip.core.util.UtilError;
import net.minecraft.world.item.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class BlockEventListener implements Listener {
    @EventHandler
    public void onBreak(@NotNull BlockBreakEvent event) {
        final ItemStack handle = Util.checkPlayer(event.getPlayer());
        if (handle == null) {
            return;
        }
        final String identifierString = handle.tag.getString("liquip:identifier");
        final Result<Identifier, UtilError> parsedIdentifier = Identifier.parse(identifierString);
        if (parsedIdentifier.isErr()) {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger()
                    .error("{BlockBreakEvent} Item with invalid " + "identifier");
            return;
        }
        final Identifier identifier = parsedIdentifier.unwrap();
        final LiquipItem liquipItem = Liquip.getProvider().getItemRegistry().get(identifier);
        if (liquipItem == null) {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger()
                    .error("{BlockBreakEvent} Item with unknown identifier");
            return;
        }
        liquipItem.callEvent(BlockBreakEvent.class, event);
    }
}
