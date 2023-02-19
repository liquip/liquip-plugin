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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SkullTextureFeature implements TaggedFeature<String> {
    private final NamespacedKey key = new NamespacedKey("minecraft", "skull_texture");

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public @Nullable String initialize(@NotNull Item item, @NotNull ConfigElement element) {
        if (!element.isString()) {
            return null;
        }
        return element.asString();
    }

    @Override
    public void apply(@NotNull Item item, @NotNull ItemStack itemStack, @NotNull String object) {
        itemStack.editMeta(it -> {
            if (it instanceof SkullMeta meta) {
                final PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
                profile.setProperty(new ProfileProperty("textures", object));
                meta.setPlayerProfile(profile);
            }
        });
    }
}
