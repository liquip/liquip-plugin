package com.github.sqyyy.liquip.features.custom;

import com.github.sqyyy.liquip.items.Feature;
import com.github.sqyyy.liquip.items.LiquipItem;
import org.bukkit.event.player.PlayerFishEvent;

public class GrapplingHook implements Feature {
    @Override
    public void initialize(LiquipItem item) {
        item.registerEvent(PlayerFishEvent.class, this::onFish);
    }

    public void onFish(PlayerFishEvent event) {
        final var state = event.getState();

        if (state != PlayerFishEvent.State.REEL_IN) {
            return;
        }

        final var velocity =
                event.getHook().getLocation().subtract(event.getPlayer().getLocation()).toVector().normalize()
                        .multiply(3);
        velocity.setY(Math.min(Math.abs(velocity.getY() * 0.65), 1));
        event.getPlayer().setVelocity(velocity);
    }
}
