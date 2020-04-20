package com.redefocus.hub.item.listener;

import com.redefocus.api.spigot.preference.event.PreferenceStateChangeEvent;
import com.redefocus.common.shared.cooldown.manager.CooldownManager;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.preference.Preference;
import com.redefocus.hub.item.enums.LobbyItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
public class PreferenceStateChangeListener implements Listener {
    @EventHandler(ignoreCancelled = false)
    public void onChange(PreferenceStateChangeEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();

        Preference preference = event.getPreference();

        if (preference != Preference.VISIBILITY) return;

        User user = event.getUser();

        LobbyItem lobbyItem = user.isEnabled(preference) ? LobbyItem.VISIBILITY_ON : LobbyItem.VISIBILITY_OFF;

        lobbyItem.give(player);

        CooldownManager.startCooldown(
                user,
                TimeUnit.SECONDS.toMillis(5),
                preference
        );
    }
}
