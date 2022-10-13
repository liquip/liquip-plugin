package com.github.sqyyy.liquip.core.event;

import com.github.sqyyy.liquip.core.Liquip;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.core.util.Identifier;
import net.minecraft.world.item.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlayerEventListener implements Listener {
    @EventHandler
    public void onInteract(@NotNull PlayerInteractEvent event) {
        final ItemStack handle = Util.checkPlayer(event.getPlayer());
        if (handle == null) {
            return;
        }
        final String identifierString = handle.tag.getString("liquip:identifier");
        final Optional<Identifier> parsedIdentifier = Identifier.parse(identifierString);
        if (parsedIdentifier.isEmpty()) {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger()
                    .error("{PlayerInteractEvent} Item with invalid identifier");
            return;
        }
        final Identifier identifier = parsedIdentifier.get();
        final LiquipItem liquipItem = Liquip.getProvider().getItemRegistry().get(identifier);
        if (liquipItem == null) {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger()
                    .error("{PlayerInteractEvent} Item with unknown identifier");
            return;
        }
        liquipItem.callEvent(PlayerInteractEvent.class, event);
    }

    @EventHandler
    public void onInteract(@NotNull PlayerInteractEntityEvent event) {
        final ItemStack handle = Util.checkPlayer(event.getPlayer());
        if (handle == null) {
            return;
        }
        final String identifierString = handle.tag.getString("liquip:identifier");
        final Optional<Identifier> parsedIdentifier = Identifier.parse(identifierString);
        if (parsedIdentifier.isEmpty()) {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger()
                    .error("{PlayerInteractEntityEvent} Item with invalid identifier");
            return;
        }
        final Identifier identifier = parsedIdentifier.get();
        final LiquipItem liquipItem = Liquip.getProvider().getItemRegistry().get(identifier);
        if (liquipItem == null) {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger()
                    .error("{PlayerInteractEntityEvent} Item with unknown identifier");
            return;
        }
        liquipItem.callEvent(PlayerInteractEntityEvent.class, event);
    }

    @EventHandler
    public void onInteract(@NotNull PlayerInteractAtEntityEvent event) {
        final ItemStack handle = Util.checkPlayer(event.getPlayer());
        if (handle == null) {
            return;
        }
        final String identifierString = handle.tag.getString("liquip:identifier");
        final Optional<Identifier> identifier = Identifier.parse(identifierString);
        if (identifier.isEmpty()) {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger()
                    .error("{PlayerInteractAtEntityEvent} Item with invalid identifier");
            return;
        }
        final LiquipItem liquipItem = Liquip.getProvider().getItemRegistry().get(identifier.get());
        if (liquipItem == null) {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger()
                    .error("{PlayerInteractAtEntityEvent} Item with unknown identifier");
            return;
        }
        liquipItem.callEvent(PlayerInteractAtEntityEvent.class, event);
    }

    @EventHandler
    public void onRod(@NotNull PlayerFishEvent event) {
        final net.minecraft.world.item.ItemStack handle = Util.checkPlayer(event.getPlayer());
        if (handle == null) {
            return;
        }
        final String identifierString = handle.tag.getString("liquip:identifier");
        final Optional<Identifier> parsedIdentifier = Identifier.parse(identifierString);
        if (parsedIdentifier.isEmpty()) {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger()
                    .error("{PlayerFishEvent} Item with invalid identifier");
            return;
        }
        final Identifier identifier = parsedIdentifier.get();
        final LiquipItem liquipItem = Liquip.getProvider().getItemRegistry().get(identifier);
        if (liquipItem == null) {
            Liquip.getProvidingPlugin(Liquip.class).getSLF4JLogger()
                    .error("{PlayerFishEvent} Item with unknown identifier");
            return;
        }
        liquipItem.callEvent(PlayerFishEvent.class, event);
    }
}
