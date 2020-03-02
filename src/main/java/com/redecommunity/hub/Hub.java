package com.redecommunity.hub;

import com.redecommunity.api.spigot.CommunityPlugin;
import com.redecommunity.hub.manager.StartManager;

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
    }

    @Override
    public void onDisablePlugin() {
    }
}
