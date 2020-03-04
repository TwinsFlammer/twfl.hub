package com.redecommunity.hub.listeners.general;

import com.redecommunity.api.spigot.scoreboard.CustomBoard;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.hub.scoreboard.manager.ScoreboardManager;
import com.redecommunity.hub.selector.item.SelectorItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
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

        ItemStack selectorItem = new SelectorItem()
                .build();

        PlayerInventory playerInventory = player.getInventory();

        playerInventory.clear();

        playerInventory.setItem(
                4,
                selectorItem
        );

        playerInventory.setHeldItemSlot(4);
    }
}
