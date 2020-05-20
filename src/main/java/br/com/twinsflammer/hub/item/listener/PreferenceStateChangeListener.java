package br.com.twinsflammer.hub.item.listener;

import br.com.twinsflammer.hub.item.enums.LobbyItem;
import br.com.twinsflammer.api.spigot.preference.event.PreferenceStateChangeEvent;
import br.com.twinsflammer.common.shared.cooldown.manager.CooldownManager;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.preference.Preference;
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
