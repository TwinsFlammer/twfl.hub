package br.com.twinsflammer.hub.listeners.general;

import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

/**
 * Created by @SrGutyerrez
 */
public class PlayerInteractAtEntityListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        User user = UserManager.getUser(player.getUniqueId());

        Entity entity = event.getRightClicked();

        if (entity instanceof ItemFrame)
            event.setCancelled(true);

        if (user.hasGroup(GroupNames.MANAGER)) return;

        event.setCancelled(true);
    }
}
