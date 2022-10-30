package com.github.sqyyy.liquip.example.features;

import com.destroystokyo.paper.ParticleBuilder;
import com.github.sqyyy.liquip.core.items.Feature;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.example.LiquipExample;
import com.github.sqyyy.liquip.example.PlayerData;
import com.github.sqyyy.liquip.example.util.LocationBuilder;
import com.github.sqyyy.liquip.example.util.Task;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StaffOfPower implements Feature {
    //private final Map<UUID, SonicShot> sonicShots = new HashMap<>();
    private final Map<UUID, Shot> shots = new HashMap<>();
    private final LiquipExample plugin;
    private final double SPEED = 2d;
    private final float YIELD = 2f;
    private final int TARGET_DISTANCE = 100;
    private final boolean GLOWING = false;
    private final boolean VISUAL_FIRE = true;

    public StaffOfPower(LiquipExample plugin) {
        this.plugin = plugin;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 0, 1);
    }

    public void tick() {
        for (Map.Entry<UUID, Shot> entry : shots.entrySet()) {
            final UUID uuid = entry.getKey();
            final Shot shot = entry.getValue();
            shot.tick(uuid);
        }
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
            throw new IllegalStateException("PlayerData for player '" + player.getName() + "' (" + player.getUniqueId() +
                    ") not found. This may be caused by a reload!");
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            shootFireballs(player, playerData, player.getEyeLocation());
            return;
        }
        spawnShots(player, playerData, player.getEyeLocation());
    }

    private void shootFireballs(Player player, PlayerData playerData, Location eyeLocation) {
        if (playerData.isStaffOfPowerOnCooldown()) {
            player.sendMessage(Component.text("Item is on cooldown").color(TextColor.color(0xDD1C1A)));
            return;
        }
        final Location target = eyeLocation.clone().add(eyeLocation.getDirection().multiply(TARGET_DISTANCE));
        final LocationBuilder vecBuilder = new LocationBuilder(eyeLocation).up(5);
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

    private void spawnShots(Player player, PlayerData playerData, Location eyeLocation) {
        if (playerData.isStaffOfPowerOnCooldown()) {
            player.sendMessage(Component.text("Item is on cooldown for " +
                            ((int) (playerData.getStaffOfPowerCooldown() - System.currentTimeMillis()) / 1000) + " second(s)")
                    .color(TextColor.color(0xDD1C1A)));
            return;
        }
        if (shots.containsKey(player.getUniqueId())) {
            player.sendMessage(Component.text("Item was just used")
                    .color(TextColor.color(0xDD1C1A)));
            return;
        }
        final RayTraceResult rayTraceResult = player.rayTraceBlocks(25);
        if (rayTraceResult == null || rayTraceResult.getHitBlock() == null) {
            playerData.cooldownStaffOfPower(1000 * 2);
            return;
        }
        final Block targetBlock = rayTraceResult.getHitBlock();
        final Shot task = new Shot(eyeLocation, targetBlock.getLocation());
        plugin.getAsyncQueue().submit(task);
        shots.put(player.getUniqueId(), task);
        playerData.cooldownStaffOfPower(1000 * 5);
    }

    public class Shot implements Task {
        private final Location location;
        private final Location target;
        private final Location[] locations = new Location[7];
        private final ArmorStand[] armorStands = new ArmorStand[7];
        private final Vector[] directions = new Vector[7];
        private volatile boolean finished = false;
        private int tick = 0;

        public Shot(Location location, Location target) {
            this.location = location;
            this.target = target;
        }

        private void spawn() {
            for (int i = 0; i < locations.length; i++) {
                armorStands[i] = spawnSingle(locations[i]);
            }
        }

        private ArmorStand spawnSingle(Location location) {
            final ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            armorStand.setPersistent(true);
            armorStand.setInvisible(true);
            armorStand.setInvulnerable(true);
            armorStand.setGravity(false);
            armorStand.setItem(EquipmentSlot.HEAD, new ItemStack(Material.CRYING_OBSIDIAN));
            return armorStand;
        }

        public void tick(UUID uuid) {
            if (!finished) {
                return;
            }
            if (tick == 0) {
                spawn();
            }
            int aliveArmorStands = 0;
            for (ArmorStand armorStand : armorStands) {
                if (armorStand != null) {
                    aliveArmorStands++;
                }
            }
            if (aliveArmorStands < 1) {
                shots.remove(uuid);
            }
            tick++;
            final ParticleBuilder particleBuilder = new ParticleBuilder(Particle.REDSTONE);
            particleBuilder.color(Color.TEAL);
            for (int i = 0; i < locations.length; i++) {
                if (armorStands[i] == null) {
                    continue;
                }
                final Location spawnLocation = locations[i].clone().add(directions[i].clone().multiply(0.3 * tick));
                if (target.distanceSquared(spawnLocation) < 1.5) {
                    final LightningStrike lightning = spawnLocation.getWorld().spawn(spawnLocation, LightningStrike.class);
                    lightning.addScoreboardTag("liquip:staff_of_power_lightning");
                    lightning.setLifeTicks(10);
                    lightning.setFlashCount(15);
                    armorStands[i].remove();
                    armorStands[i] = null;
                }
                particleBuilder.location(spawnLocation);
                particleBuilder.receivers(25);
                particleBuilder.spawn();
            }
        }

        @Override
        public boolean isFinished() {
            return finished;
        }

        @Override
        public void run() {
            final LocationBuilder vec = new LocationBuilder(location);
            vec.up(5);
            locations[0] = vec.clone().left(5).build();
            directions[0] = target.toVector().subtract(locations[0].toVector()).normalize();
            locations[1] = vec.clone().right(5).build();
            directions[1] = target.toVector().subtract(locations[1].toVector()).normalize();
            vec.up(2);
            locations[2] = vec.clone().left(4).build();
            directions[2] = target.toVector().subtract(locations[2].toVector()).normalize();
            locations[3] = vec.clone().right(4).build();
            directions[3] = target.toVector().subtract(locations[3].toVector()).normalize();
            vec.up(1);
            locations[4] = vec.clone().left(2).build();
            directions[4] = target.toVector().subtract(locations[4].toVector()).normalize();
            locations[5] = vec.clone().right(2).build();
            directions[5] = target.toVector().subtract(locations[5].toVector()).normalize();
            locations[6] = vec.build();
            directions[6] = target.toVector().subtract(locations[6].toVector()).normalize();
            finished = true;
        }
    }
}
