package com.redecommunity.hub.selector.listener;

import com.redecommunity.api.spigot.hologram.data.CustomHologram;
import com.redecommunity.api.spigot.hologram.line.TextHologramLine;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.hub.selector.data.ServerInfo;
import com.redecommunity.hub.selector.manager.ServerInfoManager;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by @SrGutyerrez
 */
public class NPCSpawnListener implements Listener {
    @EventHandler
    public void onSpawn(NPCSpawnEvent event) {
        NPC npc = event.getNPC();

        ServerInfo serverInfo = ServerInfoManager.getServerInfo(npc);

        if (serverInfo == null) return;

        Location location = npc.getStoredLocation().clone();

        location.setY(location.getY() + 0.5);

        Server server = serverInfo.getServer();

        CustomHologram customHologram = new CustomHologram(location);

        customHologram.appendLine(
                new TextHologramLine("§e" + server.getDisplayName())
        );
        customHologram.appendLine(
                new TextHologramLine("§b" + server.getPlayerCount() + "/" + server.getSlots())
        );

        serverInfo.setHologram(customHologram);
    }
}
