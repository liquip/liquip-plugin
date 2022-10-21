package com.github.sqyyy.liquip.example.util;

import org.bukkit.Location;

public class VectorBuilder {
    private final Location location;

    public VectorBuilder(Location location) {
        this.location = location.clone();
    }

    public VectorBuilder up(double blocks) {
        location.add(0, blocks, 0);
        return this;
    }

    public VectorBuilder down(double blocks) {
        location.subtract(0, blocks, 0);
        return this;
    }

    public VectorBuilder right(double blocks) {
        final Location rotatedLocation = location.clone();
        rotatedLocation.setYaw(rotatedLocation.getYaw() + 90f);
        rotatedLocation.setPitch(0);
        location.add(rotatedLocation.getDirection().multiply(blocks));
        return this;
    }

    public VectorBuilder left(double blocks) {
        final Location rotatedLocation = location.clone();
        rotatedLocation.setYaw(rotatedLocation.getYaw() - 90f);
        rotatedLocation.setPitch(0);
        location.add(rotatedLocation.getDirection().multiply(blocks));
        return this;
    }

    public VectorBuilder forward(double blocks) {
        final Location rotatedLocation = location.clone();
        rotatedLocation.setPitch(0);
        location.add(rotatedLocation.getDirection().multiply(blocks));
        return this;
    }

    public VectorBuilder backward(double blocks) {
        final Location rotatedLocation = location.clone();
        rotatedLocation.setPitch(0);
        location.add(rotatedLocation.getDirection().multiply(-blocks));
        return this;
    }

    public VectorBuilder ahead(double blocks) {
        location.add(location.getDirection().multiply(blocks));
        return this;
    }

    public VectorBuilder behind(double blocks) {
        location.add(location.getDirection().multiply(-blocks));
        return this;
    }

    public VectorBuilder turnUp(float degrees) {
        location.setPitch((location.getPitch() + 90f - degrees) % 180f - 90);
        return this;
    }

    public VectorBuilder turnDown(float degrees) {
        location.setPitch((location.getPitch() + 90f + degrees) % 180f - 90);
        return this;
    }

    public VectorBuilder turnRight(float degrees) {
        location.setYaw((location.getYaw() + degrees) % 360f);
        return this;
    }

    public VectorBuilder turnLeft(float degrees) {
        location.setYaw((location.getYaw() - degrees) % 360f);
        return this;
    }

    public Location build() {
        return location.clone();
    }
}
