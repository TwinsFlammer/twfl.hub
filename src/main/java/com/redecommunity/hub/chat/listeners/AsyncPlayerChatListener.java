package com.redecommunity.hub.chat.listeners;

import com.redecommunity.api.shared.log.dao.LogDao;
import com.redecommunity.api.shared.log.data.Log;
import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by @SrGutyerrez
 */
public class AsyncPlayerChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        User user = UserManager.getUser(player.getUniqueId());

        event.setCancelled(true);

        if (!user.hasGroup(GroupNames.HELPER)) return;

        String message = String.format(
                "%s§f%s §c» §7%s",
                user.getPrefix(),
                user.getDisplayName(),
                event.getMessage()
        );

        Log log = new Log(
                user.getId(),
                false,
                System.currentTimeMillis(),
                "SURVIVAL",
                user.getServer().getId(),
                Log.LogType.CHAT,
                Log.LogType.DEFAULT,
                event.getMessage()
        );

        LogDao logDao = new LogDao();

        logDao.insert(log);

        Bukkit.getOnlinePlayers().forEach(player1 -> {
            player1.sendMessage(message);
        });
    }
}
