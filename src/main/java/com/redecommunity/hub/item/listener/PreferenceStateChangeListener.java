package com.redecommunity.hub.item.listener;

import com.redecommunity.api.spigot.preference.event.PreferenceStateChangeEvent;
import com.redecommunity.common.shared.preference.Preference;
import com.redecommunity.hub.item.LobbyItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;

/**
 * Created by @SrGutyerrez
 */
public class PreferenceStateChangeListener implements Listener {
    @EventHandler
    public void onChange(PreferenceStateChangeEvent event) {
        Player player = event.getPlayer();

        Preference preference = event.getPreference();

        if (preference != Preference.VISIBILITY) return;

        Arrays.stream(LobbyItem.values())
                .filter(lobbyItem -> lobbyItem.name().startsWith("INVISIBILITY_"))
                .forEach(lobbyItem -> {
                    lobbyItem.give(player);
                });
    }
}
