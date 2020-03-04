package com.redecommunity.hub.listeners.general;

import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.hub.selector.inventory.SelectorInventory;
import com.redecommunity.hub.selector.item.SelectorItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by @SrGutyerrez
 */
public class PlayerInteractListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        User user = UserManager.getUser(player.getUniqueId());

        if (user.hasGroup(GroupNames.MANAGER)) return;

        event.setCancelled(true);

        ItemStack inHand = player.getItemInHand(),
                selectorItem = new SelectorItem().build();

        System.out.println("Listener");

        if (inHand.isSimilar(selectorItem)) {
            System.out.println("é similar");

            player.openInventory(
                    new SelectorInventory()
                            .build()
            );
            return;
        }
    }
}
