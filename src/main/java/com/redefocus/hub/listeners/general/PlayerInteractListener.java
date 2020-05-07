package com.redefocus.hub.listeners.general;

import com.redefocus.api.spigot.preference.event.PreferenceStateChangeEvent;
import com.redefocus.api.spigot.util.Cuboid;
import com.redefocus.api.spigot.util.action.data.CustomAction;
import com.redefocus.api.spigot.util.jsontext.data.JSONText;
import com.redefocus.common.shared.Common;
import com.redefocus.common.shared.cooldown.manager.CooldownManager;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.common.shared.preference.Preference;
import com.redefocus.common.shared.server.enums.ServerType;
import com.redefocus.common.shared.util.TimeFormatter;
import com.redefocus.hub.item.enums.LobbyItem;
import com.redefocus.hub.selector.inventory.SelectorInventory;
import org.bukkit.Material;
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

        Block block = event.getClickedBlock();

        if (block != null && block.getType() == Material.ITEM_FRAME || block.getType() == Material.MAP) {
            event.setCancelled(true);

            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);

            Integer x = block.getX(), y = block.getY(), z = block.getZ();

            Cuboid cuboid = new Cuboid(
                    9,
                    40,
                    -3,
                    10,
                    44,
                    4,
                    player.getWorld()
            );

            if (cuboid.contains(x, y, z)) {
                if (CooldownManager.inCooldown(user, "HUB_ITEM_FRAME")) return;

                new JSONText()
                        .text("\n")
                        .next()
                        .text("  §aClique ")
                        .next()
                        .text("§a§lAQUI")
                        .clickOpenURL(Common.SERVER_URL)
                        .next()
                        .text(" §r§apara acessar o site!")
                        .next()
                        .text("\n")
                        .next()
                        .send(player);

                CooldownManager.startCooldown(
                        user,
                        TimeUnit.SECONDS.toMillis(1),
                        "HUB_ITEM_FRAME"
                );
            }
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
