package io.github.liquip.paper.core.item.feature.minecraft;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.github.liquip.api.config.ConfigElement;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SkullTextureFeature implements TaggedFeature<String> {
    private final NamespacedKey key = new NamespacedKey("minecraft", "skull_texture");

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public @Nullable String initialize(@NonNull Item item, @NonNull ConfigElement element) {
        if (!element.isString()) {
            return null;
        }
        return element.asString();
    }

    @Override
    public void apply(@NonNull Item item, @NonNull ItemStack itemStack, @NonNull String object) {
        itemStack.editMeta(it -> {
            if (it instanceof SkullMeta meta) {
                final PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
                profile.setProperty(new ProfileProperty("textures", object));
                meta.setPlayerProfile(profile);
            }
        });
    }
}
