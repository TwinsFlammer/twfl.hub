package com.redecommunity.hub.listeners.general;

import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by @SrGutyerrez
 */
public class InventoryClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Inventory clickedInventory = event.getClickedInventory();

        User user = UserManager.getUser(player.getUniqueId());

        if (clickedInventory instanceof PlayerInventory && !user.hasGroup(GroupNames.DIRECTOR))
            event.setCancelled(true);
    }
}
