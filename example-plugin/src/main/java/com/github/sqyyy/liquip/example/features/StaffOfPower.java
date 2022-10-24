package com.github.sqyyy.liquip.example.features;

import com.github.sqyyy.liquip.core.items.Feature;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.example.LiquipExample;
import com.github.sqyyy.liquip.example.PlayerData;
import com.github.sqyyy.liquip.example.util.VectorBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class StaffOfPower implements Feature {
    private final LiquipExample plugin;
    private final double SPEED = 2d;
    private final float YIELD = 2f;
    private final int TARGET_DISTANCE = 50;
    private final boolean GLOWING = false;
    private final boolean VISUAL_FIRE = true;

    public StaffOfPower(LiquipExample plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize(LiquipItem item) {
        item.registerEvent(PlayerInteractEvent.class, this::onInteract);
    }

    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        event.setCancelled(true);
        final Player player = event.getPlayer();
        final PlayerData playerData = plugin.getPlayerData().get(player.getUniqueId());
        if (playerData == null) {
            throw new IllegalStateException(
                    "PlayerData for player '" + player.getName() + "' (" + player.getUniqueId() +
                            ") not found. This may be caused by a reload!");
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            shootFireballs(player, playerData, player.getEyeLocation());
            return;
        }
        spawnSonicShots(player, playerData, player.getEyeLocation());
    }

    private void shootFireballs(Player player, PlayerData playerData, Location eyeLocation) {
        if (playerData.isStaffOfPowerOnCooldown()) {
            player.sendMessage(Component.text("Item is on cooldown").color(TextColor.color(0xDD1C1A)));
            return;
        }
        final Location target = eyeLocation.clone().add(eyeLocation.getDirection().multiply(TARGET_DISTANCE));
        final VectorBuilder vecBuilder = new VectorBuilder(eyeLocation).up(5);
        shootFireball(vecBuilder.clone().left(5).build(), target, YIELD);
        shootFireball(vecBuilder.clone().right(5).build(), target, YIELD);
        vecBuilder.up(2);
        shootFireball(vecBuilder.clone().left(4).build(), target, YIELD);
        shootFireball(vecBuilder.clone().right(4).build(), target, YIELD);
        vecBuilder.up(1);
        shootFireball(vecBuilder.clone().left(2).build(), target, YIELD);
        shootFireball(vecBuilder.clone().right(2).build(), target, YIELD);
        shootFireball(vecBuilder.build(), target, YIELD);
        playerData.cooldownStaffOfPower(50 * 10);
    }

    private void shootFireball(Location location, Location target, float yield) {
        final Fireball fireball = (Fireball) location.getWorld().spawnEntity(location, EntityType.FIREBALL);
        fireball.setGlowing(GLOWING);
        fireball.setVisualFire(VISUAL_FIRE);
        fireball.setYield(yield);
        fireball.setPersistent(false);
        fireball.setDirection(target.toVector().subtract(location.toVector()).normalize().multiply(SPEED));
    }

    private void spawnSonicShots(Player player, PlayerData playerData, Location eyeLocation) {
        if (playerData.isStaffOfPowerOnCooldown()) {
            player.sendMessage(Component.text("Item is on cooldown for " +
                            ((int) (playerData.getStaffOfPowerCooldown() - System.currentTimeMillis()) / 1000) + " second(s)")
                    .color(TextColor.color(0xDD1C1A)));
            return;
        }
        final VectorBuilder vecBuilder = new VectorBuilder(eyeLocation).up(5);
        final SonicShot shot = new SonicShot(eyeLocation /* todo target */, new ArmorStand[7]);
        shot.armorStands[0] = spawnArmorStand(vecBuilder.clone().left(5).build());
        shot.armorStands[1] = spawnArmorStand(vecBuilder.clone().right(5).build());
        vecBuilder.up(2);
        shot.armorStands[2] = spawnArmorStand(vecBuilder.clone().left(4).build());
        shot.armorStands[3] = spawnArmorStand(vecBuilder.clone().right(4).build());
        vecBuilder.up(1);
        shot.armorStands[4] = spawnArmorStand(vecBuilder.clone().left(2).build());
        shot.armorStands[5] = spawnArmorStand(vecBuilder.clone().right(2).build());
        shot.armorStands[6] = spawnArmorStand(vecBuilder.build());
        playerData.setStaffOfPowerSonicShot(shot);
        playerData.cooldownStaffOfPower(20 * 20 * 5);
    }

    private ArmorStand spawnArmorStand(Location location) {
        final ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setPersistent(true);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setItem(EquipmentSlot.HEAD, new ItemStack(Material.CRYING_OBSIDIAN));
        return armorStand;
    }

    public record SonicShot(Location target, ArmorStand[] armorStands) {
    }
}
