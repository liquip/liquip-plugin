package com.github.sqyyy.liquip.example.features;

import com.github.sqyyy.liquip.core.items.Feature;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.example.util.VectorBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

public class StaffOfPower implements Feature {
    private final double UP = 5d;
    private final double RIGHT = 5d;
    private final double LEFT = 5d;
    private final double SPEED = 3d;
    private final int TARGET_DISTANCE = 25;
    private final boolean GLOWING = true;
    private final boolean VISUAL_FIRE = true;

    @Override
    public void initialize(LiquipItem item) {
        item.registerEvent(PlayerInteractEvent.class, this::onInteract);
    }

    public void onInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        final Player player = event.getPlayer();
        final World world = player.getWorld();
        final Vector target =
                player.getLocation().clone().add(player.getLocation().getDirection().multiply(TARGET_DISTANCE)).toVector();
        final Location midElementPos = new VectorBuilder(player.getEyeLocation()).up(UP).build();
        final Location rightElementPos = new VectorBuilder(player.getEyeLocation()).up(UP).right(RIGHT).build();
        final Location leftElementPos = new VectorBuilder(player.getEyeLocation()).up(UP).left(LEFT).build();
        final Fireball midEntity = (Fireball) world.spawnEntity(midElementPos, EntityType.FIREBALL);
        final Fireball rightEntity = (Fireball) world.spawnEntity(rightElementPos, EntityType.FIREBALL);
        final Fireball leftEntity = (Fireball) world.spawnEntity(leftElementPos, EntityType.FIREBALL);
        midEntity.setGlowing(GLOWING);
        rightEntity.setGlowing(GLOWING);
        leftEntity.setGlowing(GLOWING);
        midEntity.setVisualFire(VISUAL_FIRE);
        rightEntity.setVisualFire(VISUAL_FIRE);
        leftEntity.setVisualFire(VISUAL_FIRE);
        midEntity.setYield(5f);
        rightEntity.setYield(8f);
        leftEntity.setYield(5f);
        midEntity.setPersistent(false);
        rightEntity.setPersistent(false);
        leftEntity.setPersistent(false);
        midEntity.setDirection(target.clone().subtract(midEntity.getLocation().toVector()).normalize().multiply(SPEED));
        rightEntity.setDirection(target.clone().subtract(rightEntity.getLocation().toVector()).normalize().multiply(SPEED));
        leftEntity.setDirection(target.clone().subtract(leftEntity.getLocation().toVector()).normalize().multiply(SPEED));
    }
}
