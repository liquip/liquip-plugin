package io.github.liquip.paper.standalone;

import io.github.liquip.paper.core.util.api.ApiRegistrationUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class LiquipPaperPlugin extends JavaPlugin {
    private final StandaloneLiquip api = new StandaloneLiquip(this);

    @Override
    public void onLoad() {
        ApiRegistrationUtil.registerProvider(this.api);
        this.api.loadSystem();
    }

    @Override
    public void onEnable() {
        this.api.enableSystem();
    }

    @Override
    public void onDisable() {
        this.api.disableSystem();
    }
}
