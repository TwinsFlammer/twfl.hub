package com.redecommunity.hub;

import com.redecommunity.api.spigot.CommunityPlugin;
import com.redecommunity.hub.manager.StartManager;
import net.minecraft.server.v1_8_R3.GameRules;
import org.bukkit.Server;
import org.bukkit.command.defaults.GameRuleCommand;

/**
 * Created by @SrGutyerrez
 */
public class Hub extends CommunityPlugin {
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
