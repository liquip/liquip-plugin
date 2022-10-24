package com.github.sqyyy.liquip.example.util;

import org.bukkit.Location;

public class VectorBuilder implements Cloneable {
    private final Location location;

    public VectorBuilder(Location location) {
        this.location = location.clone();
    }

    public VectorBuilder pitch(float pitch) {
        location.setPitch(pitch);
        return this;
    }

    public VectorBuilder yaw(float yaw) {
        location.setYaw(yaw);
        return this;
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
        rotatedLocation.setYaw((rotatedLocation.getYaw() + 180f + 90f) % 360f - 180f);
        rotatedLocation.setPitch(0);
        location.add(rotatedLocation.getDirection().multiply(blocks));
        return this;
    }

    public VectorBuilder left(double blocks) {
        final Location rotatedLocation = location.clone();
        rotatedLocation.setYaw((rotatedLocation.getYaw() + 180f - 90f) % 360f - 180f);
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

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public VectorBuilder clone() {
        return new VectorBuilder(location.clone());
    }

    public Location build() {
        return location;
    }
}
