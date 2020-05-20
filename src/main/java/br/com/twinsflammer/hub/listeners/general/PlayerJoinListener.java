package br.com.twinsflammer.hub.listeners.general;

import br.com.twinsflammer.hub.item.enums.LobbyItem;
import br.com.twinsflammer.hub.selector.NPCMethods;
import br.com.twinsflammer.api.spigot.scoreboard.CustomBoard;
import br.com.twinsflammer.api.spigot.spawn.manager.SpawnManager;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.preference.Preference;
import br.com.twinsflammer.hub.scoreboard.manager.ScoreboardManager;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
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

        if (user.hasGroup(GroupNames.ELITE)) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }

        PlayerInventory playerInventory = player.getInventory();

        playerInventory.clear();

        for (LobbyItem lobbyItem : LobbyItem.values())
            lobbyItem.give(player);

        playerInventory.setHeldItemSlot(4);

        CitizensAPI.getNPCRegistry().sorted().forEach(npc -> NPCMethods.hide(npc, player));

        Bukkit.getOnlinePlayers().forEach(player1 -> {
            User user1 = UserManager.getUser(player1.getUniqueId());

            if (user1.isDisabled(Preference.VISIBILITY) && !user.hasGroup(GroupNames.HELPER))
                player1.hidePlayer(player);
        });
    }
}
