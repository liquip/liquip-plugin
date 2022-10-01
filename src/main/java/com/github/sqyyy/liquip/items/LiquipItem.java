package com.github.sqyyy.liquip.items;

import com.github.sqyyy.liquip.util.Identifier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface LiquipItem {
    Identifier getKey();

    Component getName();

    Material getMaterial();

    List<Component> getLore();

    List<LeveledEnchantment> getEnchantments();

    List<Feature> getFeatures();

    ItemStack newItem();

    class Builder {
        private Identifier key = null;
        private Component name = null;
        private Material material = null;
        private List<Component> lore = new ArrayList<>();
        private List<LeveledEnchantment> enchantments = new ArrayList<>();
        private List<Feature> features = new ArrayList<>();

        public Builder() {
        }

        public Builder(Identifier key, String name, Material material) {
            this.key = key;
            this.name = MiniMessage.miniMessage().deserialize(name);
            this.material = material;
        }

        public Builder identifier(Identifier identifier) {
            this.key = identifier;
            return this;
        }

        public Builder name(String name) {
            this.name = MiniMessage.miniMessage().deserialize(name);
            return this;
        }

        public Builder name(Component name) {
            this.name = name;
            return this;
        }

        public Builder material(Material material) {
            this.material = material;
            return this;
        }

        public Builder lore(List<Component> lore) {
            if (lore == null) {
                this.lore = new ArrayList<>();
                return this;
            }
            this.lore = lore;
            return this;
        }

        public Builder loreLine(Component line) {
            if (line == null) {
                lore.add(Component.newline());
                return this;
            }
            lore.add(line);
            return this;
        }

        public Builder enchantments(List<LeveledEnchantment> enchantments) {
            if (enchantments == null) {
                this.enchantments = new ArrayList<>();
                return this;
            }
            this.enchantments = enchantments;
            return this;
        }

        public Builder enchantment(LeveledEnchantment enchantment) {
            if (enchantment == null) {
                return this;
            }
            enchantments.add(enchantment);
            return this;
        }

        public Builder features(List<Feature> features) {
            if (features == null) {
                this.features = new ArrayList<>();
                return this;
            }
            this.features = features;
            return this;
        }

        public Builder indirectFeatures(List<Identifier> features) {
            return this;
        }

        public Builder feature(Feature feature) {
            if (feature == null) {
                return this;
            }
            features.add(feature);
            return this;
        }

        public Builder indirectFeature(Identifier feature) {
            return this;
        }

        public LiquipItem build() {
            if (key == null) {
                return null;
            }
            if (name == null) {
                return null;
            }
            if (material == null) {
                return null;
            }
            return new BasicLiquipItem(key, name, material, lore, enchantments, features);
        }
    }
}
