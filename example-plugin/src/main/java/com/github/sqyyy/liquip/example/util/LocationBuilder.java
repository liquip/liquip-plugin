package com.github.sqyyy.liquip.example.util;

import org.bukkit.Location;

public class LocationBuilder implements Cloneable {
    private final Location location;

    public LocationBuilder(Location location) {
        this.location = location.clone();
    }

    public LocationBuilder pitch(float pitch) {
        location.setPitch(pitch);
        return this;
    }

    public LocationBuilder yaw(float yaw) {
        location.setYaw(yaw);
        return this;
    }

    public LocationBuilder up(double blocks) {
        location.add(0, blocks, 0);
        return this;
    }

    public LocationBuilder down(double blocks) {
        location.subtract(0, blocks, 0);
        return this;
    }

    public LocationBuilder right(double blocks) {
        final Location rotatedLocation = location.clone();
        rotatedLocation.setYaw((rotatedLocation.getYaw() + 180f + 90f) % 360f - 180f);
        rotatedLocation.setPitch(0);
        location.add(rotatedLocation.getDirection().multiply(blocks));
        return this;
    }

    public LocationBuilder left(double blocks) {
        final Location rotatedLocation = location.clone();
        rotatedLocation.setYaw((rotatedLocation.getYaw() + 180f - 90f) % 360f - 180f);
        rotatedLocation.setPitch(0);
        location.add(rotatedLocation.getDirection().multiply(blocks));
        return this;
    }

    public LocationBuilder forward(double blocks) {
        final Location rotatedLocation = location.clone();
        rotatedLocation.setPitch(0);
        location.add(rotatedLocation.getDirection().multiply(blocks));
        return this;
    }

    public LocationBuilder backward(double blocks) {
        final Location rotatedLocation = location.clone();
        rotatedLocation.setPitch(0);
        location.add(rotatedLocation.getDirection().multiply(-blocks));
        return this;
    }

    public LocationBuilder ahead(double blocks) {
        location.add(location.getDirection().multiply(blocks));
        return this;
    }

    public LocationBuilder behind(double blocks) {
        location.add(location.getDirection().multiply(-blocks));
        return this;
    }

    public LocationBuilder turnUp(float degrees) {
        location.setPitch((location.getPitch() + 90f - degrees) % 180f - 90);
        return this;
    }

    public LocationBuilder turnDown(float degrees) {
        location.setPitch((location.getPitch() + 90f + degrees) % 180f - 90);
        return this;
    }

    public LocationBuilder turnRight(float degrees) {
        location.setYaw((location.getYaw() + degrees) % 360f);
        return this;
    }

    public LocationBuilder turnLeft(float degrees) {
        location.setYaw((location.getYaw() - degrees) % 360f);
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public LocationBuilder clone() {
        return new LocationBuilder(location.clone());
    }

    public Location build() {
        return location;
    }
}
