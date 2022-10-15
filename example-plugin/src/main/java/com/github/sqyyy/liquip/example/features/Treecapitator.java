package com.github.sqyyy.liquip.example.features;

import com.github.sqyyy.liquip.core.items.Feature;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.example.LiquipExample;
import com.github.sqyyy.liquip.example.util.BlockRelative;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class Treecapitator implements Feature {
    private final Queue queue;

    public Treecapitator(@NotNull final Queue queue) {
        this.queue = queue;
    }

    public void start() {
        queue.start(LiquipExample.getProvidingPlugin(LiquipExample.class), 2);
    }

    @Override
    public void initialize(LiquipItem item) {
        item.registerEvent(BlockBreakEvent.class, this::onBlockBreak);
    }

    public void onBlockBreak(BlockBreakEvent event) {
        if (!Tag.LOGS.isTagged(event.getBlock().getType())) {
            return;
        }
        new QueueElementRoot(64, null).iterate(event.getBlock());
    }

    public static class Queue extends BukkitRunnable {
        private final Deque<QueueElement> deque;
        private final int stepSize;

        public Queue(final int size, final int stepSize) {
            this.deque = new ArrayDeque<>(size);
            this.stepSize = stepSize;
        }

        public void start(@NotNull final Plugin plugin, final int period) {
            runTaskTimer(plugin, 0, period);
        }

        public void queue(@NotNull final QueueElement element) {
            deque.offer(element);
        }

        @Override
        public void run() {
            for (int i = 0; i < Math.min(stepSize, deque.size()); i++) {
                final QueueElement poll = deque.poll();
                poll.execute();
            }
        }
    }

    public static class QueueElement {
        private final QueueElementRoot root;
        private final Block block;

        public QueueElement(@NotNull final QueueElementRoot root, @NotNull final Block block) {
            this.root = root;
            this.block = block;
        }

        public void execute() {
            if (!Tag.LOGS.isTagged(block.getType())) {
                return;
            }
            telekinesis:
            if (root.telekinesisTarget != null) {
                final PlayerInventory inventory = root.telekinesisTarget.getInventory();
                final int firstEmpty = inventory.firstEmpty();
                if (firstEmpty == -1) {
                    break telekinesis;
                }
                final World world = block.getWorld();
                final Location location = block.getLocation();
                for (final ItemStack drop : block.getDrops(inventory.getItemInMainHand())) {
                    final int dropFirstEmpty = inventory.firstEmpty();
                    if (dropFirstEmpty == -1) {
                        world.dropItem(location, drop);
                        continue;
                    }
                    inventory.addItem(drop);
                }
                block.setType(Material.AIR);
                root.iterate(block);
                return;
            }
            final World world = block.getWorld();
            final Location location = block.getLocation();
            for (final ItemStack drop : block.getDrops()) {
                world.dropItem(location, drop);
            }
            block.setType(Material.AIR);
            root.iterate(block);
        }
    }

    public class QueueElementRoot {
        private final int maxBlocks;
        private final Player telekinesisTarget;
        private final Set<Block> blocks;
        private int iterations;

        public QueueElementRoot(final int maxBlocks, @Nullable final Player telekinesisTarget) {
            this.maxBlocks = maxBlocks;
            iterations = 0;
            this.telekinesisTarget = telekinesisTarget;
            blocks = new HashSet<>();
        }

        public void iterate(@NotNull Block block) {
            final BlockRelative[] blockRelatives = BlockRelative.values();
            for (int i = 0; i < Math.min(maxBlocks - iterations, blockRelatives.length); i++) {
                final BlockRelative blockRelative = blockRelatives[i];
                final Block iterBlock = blockRelative.getRelative(block);
                if (blocks.contains(iterBlock)) {
                    continue;
                }
                if (Tag.LOGS.isTagged(iterBlock.getType())) {
                    iterations++;
                    blocks.add(iterBlock);
                    queue.queue(new QueueElement(this, iterBlock));
                }
            }
        }
    }
}
