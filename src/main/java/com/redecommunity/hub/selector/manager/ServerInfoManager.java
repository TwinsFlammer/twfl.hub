package com.redecommunity.hub.selector.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.hub.Hub;
import com.redecommunity.hub.selector.dao.ServerInfoDAO;
import com.redecommunity.hub.selector.data.ServerInfo;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class ServerInfoManager {
    private static List<ServerInfo> servers = Lists.newArrayList();

    public ServerInfoManager() {
        ServerInfoDAO serverInfoDAO = new ServerInfoDAO();

        Bukkit.getScheduler().runTaskTimer(
                Hub.getInstance(),
                () -> {
                    Set<ServerInfo> serverInfoSet = serverInfoDAO.findAll();

                    if (ServerInfoManager.servers.isEmpty())
                        ServerInfoManager.servers.addAll(serverInfoSet);
                    else {
                        serverInfoSet.forEach(serverInfo -> {
                            ServerInfo serverInfo1 = ServerInfoManager.getServerInfo(serverInfo.getId());

                            if (serverInfo1 == null) {
                                ServerInfoManager.servers.add(serverInfo);

                                NPC npc = ServerInfoManager.getNPC(serverInfo);

                                if (npc != null)
                                    serverInfo.spawn();
                            } else {
                                if (!serverInfo.isSimilar(serverInfo1)) {
                                    serverInfo1.update(serverInfo);
                                }

                                try {
                                    serverInfo1.updateHologram();
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            }
                        });
                    }
                },
                0,
                20L * 15
        );
    }

    public static List<ServerInfo> getServers() {
        return ServerInfoManager.servers;
    }

    public static ServerInfo getServerInfo(Integer id) {
        return ServerInfoManager.servers
                .stream()
                .filter(Objects::nonNull)
                .filter(serverInfo -> serverInfo.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static ServerInfo getServerInfo(NPC npc) {
        return ServerInfoManager.servers
                .stream()
                .filter(Objects::nonNull)
                .filter(serverInfo -> serverInfo.getNpcId().equals(npc.getId()))
                .findFirst()
                .orElse(null);
    }

    public static NPC getNPC(ServerInfo serverInfo) {
        return CitizensAPI.getNPCRegistry().getById(serverInfo.getNpcId());
    }

    public static void removeFromQueue(User user) {
        ServerInfoManager.servers.forEach(serverInfo -> {
            Queue<Integer> queue = serverInfo.getQueue();

            queue.removeIf(userId -> userId.equals(user.getId()));
        });
    }

    public static ServerInfo toServerInfo(ResultSet resultSet) throws SQLException {
        return new ServerInfo(
                resultSet.getInt("id"),
                resultSet.getInt("npc_id"),
                resultSet.getInt("slot"),
                resultSet.getInt("server_id"),
                resultSet.getBoolean("active"),
                Material.getMaterial(
                        resultSet.getInt("material_id")
                ),
                resultSet.getString("description"),
                null,
                Queues.newArrayDeque()
        );
    }
}
