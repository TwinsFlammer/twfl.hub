package br.com.twinsflammer.hub;

import br.com.twinsflammer.hub.manager.StartManager;
import br.com.twinsflammer.api.spigot.FocusPlugin;
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
