package com.redecommunity.hub.selector.listener;

import com.redecommunity.hub.selector.NPCMethods;
import com.redecommunity.hub.selector.data.ServerInfo;
import com.redecommunity.hub.selector.manager.ServerInfoManager;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.event.NPCPushEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by @SrGutyerrez
 */
public class NPCListener implements Listener {
    @EventHandler
    public void onSpawn(NPCSpawnEvent event) {
        NPC npc = event.getNPC();

        Bukkit.getOnlinePlayers().forEach(player -> NPCMethods.hide(npc, player));

        ServerInfo serverInfo = ServerInfoManager.getServerInfo(npc);

        if (serverInfo == null) return;

        serverInfo.spawn();
    }

    @EventHandler
    public void onDespawn(NPCDespawnEvent event) {
        NPC npc = event.getNPC();

        ServerInfo serverInfo = ServerInfoManager.getServerInfo(npc);

        if (serverInfo == null) return;

        serverInfo.despawn();
    }

    @EventHandler
    public void onTeleport(NPCPushEvent event) {
        NPC npc = event.getNPC();

        Bukkit.getOnlinePlayers().forEach(player -> NPCMethods.hide(npc, player));

        ServerInfo serverInfo = ServerInfoManager.getServerInfo(npc);

        if (serverInfo == null) return;

        serverInfo.teleportHologram();
    }
}
