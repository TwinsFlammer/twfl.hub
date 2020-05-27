package br.com.twinsflammer.hub.listeners.general;

import br.com.twinsflammer.api.spigot.preference.event.PreferenceStateChangeEvent;
import br.com.twinsflammer.api.spigot.util.Cuboid;
import br.com.twinsflammer.api.spigot.util.action.data.CustomAction;
import br.com.twinsflammer.api.spigot.util.jsontext.data.JSONText;
import br.com.twinsflammer.common.shared.Common;
import br.com.twinsflammer.common.shared.cooldown.manager.CooldownManager;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.preference.Preference;
import br.com.twinsflammer.common.shared.server.enums.ServerType;
import br.com.twinsflammer.common.shared.util.TimeFormatter;
import br.com.twinsflammer.hub.item.enums.LobbyItem;
import br.com.twinsflammer.hub.selector.inventory.SelectorInventory;
import org.bukkit.entity.Player;
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
