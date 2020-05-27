package br.com.twinsflammer.hub.listeners.general;

import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

/**
 * @author SrGutyerrez
 */
public class HangingBreakByEntityListener implements Listener {
    @EventHandler
    public void onHanging(HangingBreakByEntityEvent event) {
        Entity remover = event.getRemover(),
                entity = event.getEntity();

        if (entity.getType() == EntityType.ITEM_FRAME) {
            Boolean cancelled = true;

            if (remover.getType() == EntityType.PLAYER) {
                Player player = (Player) remover;

                User user = UserManager.getUser(player.getUniqueId());

                cancelled = !user.hasGroup(GroupNames.DIRECTOR);
            }

            event.setCancelled(cancelled);
        }
    }
}
