package com.redecommunity.hub.selector;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Created by @SrGutyerrez
 */
public class NPCMethods {
    public static void hide(NPC npc, Player player) {
        if (npc == null || npc.getEntity() == null) return;

        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam("ZZZ") == null ? scoreboard.registerNewTeam("ZZZ") : scoreboard.getTeam("ZZZ");
        team.setNameTagVisibility(NameTagVisibility.NEVER);
        team.setCanSeeFriendlyInvisibles(false);
        team.setPrefix("§8[NPC] ");
        team.addEntry(npc.getEntity().getName());
    }
}
