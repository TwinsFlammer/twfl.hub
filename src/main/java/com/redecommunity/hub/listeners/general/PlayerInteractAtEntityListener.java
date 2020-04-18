package com.redecommunity.hub.listeners.general;

import com.redecommunity.api.spigot.util.JSONText;
import com.redecommunity.common.shared.Common;
import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
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

        System.out.println("interagiu");

        if (entity instanceof ItemFrame) {
            event.setCancelled(true);

            new JSONText()
                    .text("\n")
                    .next()
                    .text("§aClique ")
                    .next()
                    .text("§lAQUI")
                    .clickOpenURL(Common.SERVER_URL)
                    .next()
                    .text("§r§apara acessar o site!")
                    .next()
                    .send(player);
        }

        if (user.hasGroup(GroupNames.MANAGER)) return;

        event.setCancelled(true);
    }
}
