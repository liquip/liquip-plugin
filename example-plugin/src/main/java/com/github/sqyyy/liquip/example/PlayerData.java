package com.github.sqyyy.liquip.example;

public class PlayerData {
    // AspectOfTheEnd
    private long aspectOfTheEndCooldown = 0;
    // StaffOfPower
    private long staffOfPowerCooldown = 0;

    // AspectOfTheEnd
    public long getAspectOfTheEndCooldown() {
        return aspectOfTheEndCooldown;
    }

    public int getAspectOfTheEndCooldownInSeconds() {
        return (int) ((aspectOfTheEndCooldown - System.currentTimeMillis()) / 1000);
    }

    public boolean isAspectOfTheEndCooldown() {
        return aspectOfTheEndCooldown > System.currentTimeMillis();
    }

    public void cooldownAspectOftTheEnd(long millis) {
        aspectOfTheEndCooldown = System.currentTimeMillis() + millis;
    }

    // StaffOfPower
    public long getStaffOfPowerCooldown() {
        return staffOfPowerCooldown;
    }

    public int getSaffOfPowerCooldownInSeconds() {
        return (int) ((staffOfPowerCooldown - System.currentTimeMillis()) / 1000);
    }

    public boolean isStaffOfPowerOnCooldown() {
        return staffOfPowerCooldown > System.currentTimeMillis();
    }

    public void cooldownStaffOfPower(long millis) {
        staffOfPowerCooldown = System.currentTimeMillis() + millis;
    }
}
