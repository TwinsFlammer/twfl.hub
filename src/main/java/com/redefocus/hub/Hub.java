package com.redefocus.hub;

import com.redefocus.api.spigot.FocusPlugin;
import com.redefocus.hub.manager.StartManager;
import org.bukkit.Server;

/**
 * Created by @SrGutyerrez
 */
public class Hub extends FocusPlugin {
    private static Hub instance;

    public Hub() {
        Hub.instance = this;
    }

    public static Hub getInstance() {
        return Hub.instance;
    }

    @Override
    public void onEnablePlugin() {
        new StartManager();

        Server server = this.getServer();

        server.getWorlds().forEach(world -> {
            world.setGameRuleValue("randomTickSpeed", "-999");
            world.setGameRuleValue("doFireTick", "false");
            world.setGameRuleValue("doDaylightCycle", "false");

            world.setTime(1200);
        });
    }

    @Override
    public void onDisablePlugin() {
    }
}
