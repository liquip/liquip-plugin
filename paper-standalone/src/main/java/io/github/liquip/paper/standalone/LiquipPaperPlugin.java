package io.github.liquip.paper.standalone;

import io.github.liquip.common.api.ApiRegistrationUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class LiquipPaperPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        ApiRegistrationUtil.registerProvider(new StandaloneLiquipImpl(this));
    }
}
