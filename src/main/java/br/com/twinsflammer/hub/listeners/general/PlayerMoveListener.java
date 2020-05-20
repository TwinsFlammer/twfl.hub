package br.com.twinsflammer.hub.listeners.general;

import br.com.twinsflammer.api.spigot.spawn.manager.SpawnManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by @SrGutyerrez
 */
public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location location = event.getTo();

        if (location.getY() < 1) {
            Location spawnLocation = SpawnManager.DEFAULT_SPAWN;

            player.teleport(spawnLocation);
        }
    }
}
