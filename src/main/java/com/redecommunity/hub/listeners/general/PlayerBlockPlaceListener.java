package com.redecommunity.hub.listeners.general;

import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by @SrGutyerrez
 */
public class PlayerBlockPlaceListener implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        User user = UserManager.getUser(player.getUniqueId());

        if (user.hasGroup(GroupNames.MANAGER)) return;

        event.setCancelled(true);
    }
}
