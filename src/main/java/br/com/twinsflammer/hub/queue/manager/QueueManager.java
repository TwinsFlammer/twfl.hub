package br.com.twinsflammer.hub.queue.manager;

import br.com.twinsflammer.hub.selector.manager.ServerInfoManager;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.server.data.Server;
import br.com.twinsflammer.hub.Hub;
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
