package com.github.sqyyy.liquip.event;

import com.github.sqyyy.liquip.Liquip;
import com.github.sqyyy.liquip.util.Identifier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerEventListener implements Listener {
    private final Liquip liquip;

    public PlayerEventListener(Liquip liquip) {
        this.liquip = liquip;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final var handle = Util.checkPlayer(event.getPlayer());

        if (handle == null) {
            return;
        }

        final var identifierString = handle.tag.getString("liquip:identifier");
        final var parsedIdentifier = Identifier.parse(identifierString);

        if (parsedIdentifier.isErr()) {
            liquip.getSLF4JLogger().error("{PlayerInteractEvent} Item with invalid identifier");
            return;
        }

        final var identifier = parsedIdentifier.unwrap();
        final var liquipItem = Liquip.getProvider().getItemRegistry().get(identifier);

        if (liquipItem == null) {
            liquip.getSLF4JLogger().error("{PlayerInteractEvent} Item with unknown identifier");
            return;
        }

        liquipItem.callEvent(PlayerInteractEvent.class, event);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        final var handle = Util.checkPlayer(event.getPlayer());

        if (handle == null) {
            return;
        }

        final var identifierString = handle.tag.getString("liquip:identifier");
        final var parsedIdentifier = Identifier.parse(identifierString);

        if (parsedIdentifier.isErr()) {
            liquip.getSLF4JLogger().error("{PlayerInteractEntityEvent} Item with invalid identifier");
            return;
        }

        final var identifier = parsedIdentifier.unwrap();
        final var liquipItem = Liquip.getProvider().getItemRegistry().get(identifier);

        if (liquipItem == null) {
            liquip.getSLF4JLogger().error("{PlayerInteractEntityEvent} Item with unknown identifier");
            return;
        }

        liquipItem.callEvent(PlayerInteractEntityEvent.class, event);
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        final var handle = Util.checkPlayer(event.getPlayer());

        if (handle == null) {
            return;
        }

        final var identifierString = handle.tag.getString("liquip:identifier");
        final var parsedIdentifier = Identifier.parse(identifierString);

        if (parsedIdentifier.isErr()) {
            liquip.getSLF4JLogger().error("{PlayerInteractAtEntityEvent} Item with invalid identifier");
            return;
        }

        final var identifier = parsedIdentifier.unwrap();
        final var liquipItem = Liquip.getProvider().getItemRegistry().get(identifier);

        if (liquipItem == null) {
            liquip.getSLF4JLogger().error("{PlayerInteractAtEntityEvent} Item with unknown identifier");
            return;
        }

        liquipItem.callEvent(PlayerInteractAtEntityEvent.class, event);
    }

    @EventHandler
    public void onRod(PlayerFishEvent event) {
        final var handle = Util.checkPlayer(event.getPlayer());

        if (handle == null) {
            return;
        }

        final var identifierString = handle.tag.getString("liquip:identifier");
        final var parsedIdentifier = Identifier.parse(identifierString);

        if (parsedIdentifier.isErr()) {
            liquip.getSLF4JLogger().error("{PlayerFishEvent} Item with invalid identifier");
            return;
        }

        final var identifier = parsedIdentifier.unwrap();
        final var liquipItem = Liquip.getProvider().getItemRegistry().get(identifier);

        if (liquipItem == null) {
            liquip.getSLF4JLogger().error("{PlayerFishEvent} Item with unknown identifier");
            return;
        }

        liquipItem.callEvent(PlayerFishEvent.class, event);
    }
}
