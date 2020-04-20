package com.redefocus.hub.selector.listener;

import com.redefocus.hub.selector.NPCMethods;
import com.redefocus.hub.selector.data.ServerInfo;
import com.redefocus.hub.selector.manager.ServerInfoManager;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.event.NPCPushEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

    @EventHandler
    public void onInteract(NPCRightClickEvent event) {
        NPC npc = event.getNPC();

        ServerInfo serverInfo = ServerInfoManager.getServerInfo(npc);

        if (serverInfo == null) return;

        Player player = event.getClicker();

        serverInfo.connect(player);
    }
}
