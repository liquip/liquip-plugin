package com.github.sqyyy.liquip.example.features;

import com.github.sqyyy.liquip.core.items.Feature;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class GrapplingHook implements Feature {
    @Override
    public void initialize(@NotNull LiquipItem item) {
        item.registerEvent(PlayerFishEvent.class, this::onFish);
    }

    public void onFish(PlayerFishEvent event) {
        final PlayerFishEvent.State state = event.getState();
        if (state != PlayerFishEvent.State.REEL_IN && state != PlayerFishEvent.State.IN_GROUND) {
            return;
        }
        final Vector velocity =
                event.getHook().getLocation().subtract(event.getPlayer().getLocation()).toVector().normalize()
                        .multiply(3);
        velocity.setY(Math.min(Math.abs(velocity.getY() * 0.65), 1));
        event.getPlayer().setVelocity(velocity);
    }
}
