package com.redecommunity.hub.listeners.general;

import com.redecommunity.api.spigot.preference.event.PreferenceStateChangeEvent;
import com.redecommunity.api.spigot.util.JSONText;
import com.redecommunity.api.spigot.util.action.data.CustomAction;
import com.redecommunity.common.shared.Common;
import com.redecommunity.common.shared.cooldown.manager.CooldownManager;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.preference.Preference;
import com.redecommunity.common.shared.server.enums.ServerType;
import com.redecommunity.common.shared.util.TimeFormatter;
import com.redecommunity.hub.item.enums.LobbyItem;
import com.redecommunity.hub.selector.inventory.SelectorInventory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by @SrGutyerrez
 */
public class PlayerInteractListener implements Listener {
    private final Double minX = 9.7, maxX = 10.0,
            minY = 39.0, maxY = 43.2,
            minZ = -2.7, maxZ = 3.7;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        User user = UserManager.getUser(player.getUniqueId());

        if (!user.hasGroup(GroupNames.MANAGER)) event.setCancelled(true);

        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();

        Integer x = block.getX(), y = block.getY(), z = block.getZ();

        if (x >= minX || x <= maxX || y >= minY || y <= maxY || z <= minZ || z >= maxZ) {
            event.setCancelled(true);

            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);

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

        PlayerInventory playerInventory = player.getInventory();

        ItemStack inHand = playerInventory.getItemInHand();

        LobbyItem lobbyItem = LobbyItem.getLobbyItem(inHand);

        if (lobbyItem == null) return;

        switch (lobbyItem) {
            case VISIBILITY_ON:
            case VISIBILITY_OFF: {
                Preference preference = Preference.VISIBILITY;

                if (CooldownManager.inCooldown(user, preference)) {
                    Language language = user.getLanguage();

                    CustomAction customAction = new CustomAction()
                            .text(
                                    String.format(
                                            language.getMessage("preference.wait"),
                                            TimeFormatter.format(CooldownManager.getRemainingTime(
                                                    user,
                                                    preference
                                            ))
                                    )
                            );

                    customAction.getSpigot().send(player);
                    return;
                }

                user.togglePreference(
                        preference,
                        user.isEnabled(preference)
                );

                PreferenceStateChangeEvent preferenceStateChangeEvent = new PreferenceStateChangeEvent(
                        user,
                        preference
                );

                preferenceStateChangeEvent.run();

                if (preferenceStateChangeEvent.isCancelled()) {
                    user.togglePreference(
                            preference,
                            user.isEnabled(preference)
                    );
                    return;
                }
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
