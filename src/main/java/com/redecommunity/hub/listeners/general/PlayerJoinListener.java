package com.redecommunity.hub.listeners.general;

import com.redecommunity.api.spigot.scoreboard.CustomBoard;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.hub.item.LobbyItem;
import com.redecommunity.hub.scoreboard.manager.ScoreboardManager;
import com.redecommunity.hub.selector.NPCMethods;
import com.redecommunity.hub.spawn.manager.SpawnManager;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by @SrGutyerrez
 */
public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        User user = UserManager.getUser(player.getUniqueId());

        CustomBoard customBoard = ScoreboardManager.create(user);

        customBoard.send(player);

        Location location = SpawnManager.DEFAULT_SPAWN;

        if (location != null) player.teleport(location);

        PlayerInventory playerInventory = player.getInventory();

        playerInventory.clear();

        for (LobbyItem lobbyItem : LobbyItem.values())
            lobbyItem.give(player);

        playerInventory.setHeldItemSlot(4);

        CitizensAPI.getNPCRegistry().sorted().forEach(npc -> NPCMethods.hide(npc, player));
    }
}
