package com.redefocus.hub.queue.manager;

import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.common.shared.server.data.Server;
import com.redefocus.hub.Hub;
import com.redefocus.hub.selector.manager.ServerInfoManager;
import org.bukkit.Bukkit;

import java.util.Queue;

/**
 * Created by @SrGutyerrez
 */
public class QueueManager {
    public QueueManager() {
        Bukkit.getScheduler().runTaskTimer(
                Hub.getInstance(),
                () -> {
                    ServerInfoManager.getServers().forEach(serverInfo -> {
                        Queue<Integer> queue = serverInfo.getQueue();

                        if (!queue.isEmpty()) {
                            Server server = serverInfo.getServer();

                            if (server.getPlayerCount() >= server.getSlots()) return;

                            Integer userId = queue.poll();

                            assert userId != null;

                            User user = UserManager.getUser(userId);

                            user.connect(server);
                        }
                    });
                },
                0,
                20L * 5
        );
    }
}
