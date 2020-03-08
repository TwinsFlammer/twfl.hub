package com.redecommunity.hub.listeners.general;

import com.redecommunity.api.spigot.preference.event.PreferenceStateChangeEvent;
import com.redecommunity.common.shared.cooldown.data.Cooldown;
import com.redecommunity.common.shared.cooldown.manager.CooldownManager;
import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.preference.Preference;
import com.redecommunity.common.shared.server.enums.ServerType;
import com.redecommunity.hub.item.LobbyItem;
import com.redecommunity.hub.selector.inventory.SelectorInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
public class PlayerInteractListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        User user = UserManager.getUser(player.getUniqueId());

        if (!user.hasGroup(GroupNames.MANAGER)) event.setCancelled(true);

        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        PlayerInventory playerInventory = player.getInventory();

        ItemStack inHand = playerInventory.getItemInHand();

        LobbyItem lobbyItem = LobbyItem.getLobbyItem(inHand);

        if (lobbyItem == null) return;

        switch (lobbyItem) {
            case VISIBILITY_ON:
            case VISIBILITY_OFF: {
                LobbyItem newLobbyItem = lobbyItem.getParent();

                PreferenceStateChangeEvent preferenceStateChangeEvent = new PreferenceStateChangeEvent(
                        user,
                        Preference.VISIBILITY
                );

                preferenceStateChangeEvent.run();

                if (preferenceStateChangeEvent.isCancelled()) return;

                if (CooldownManager.inCooldown(user, Preference.VISIBILITY)) return;

                CooldownManager.startCooldown(
                        user,
                        TimeUnit.SECONDS.toMillis(5),
                        Preference.VISIBILITY
                );

                user.togglePreference(Preference.VISIBILITY, user.isEnabled(Preference.VISIBILITY));

                newLobbyItem.give(player);
                return;
            }
            case SERVER_SELECTOR: {
                player.openInventory(
                        new SelectorInventory()
                                .build()
                );
                return;
            }
            case LOBBY: {
                player.openInventory(
                        new SelectorInventory(ServerType.LOBBY, user)
                                .build()
                );
                return;
            }
        }
    }
}
