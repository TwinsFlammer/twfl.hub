package com.redecommunity.hub.selector.manager;

import com.google.common.collect.Lists;
import com.redecommunity.common.shared.Common;
import com.redecommunity.hub.selector.dao.ServerInfoDAO;
import com.redecommunity.hub.selector.data.ServerInfo;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
public class ServerInfoManager {
    private static List<ServerInfo> servers = Lists.newArrayList();

    public ServerInfoManager() {
        ServerInfoDAO serverInfoDAO = new ServerInfoDAO();

        Common.getInstance().getScheduler().scheduleAtFixedRate(
                () -> {
                    Set<ServerInfo> serverInfoSet = serverInfoDAO.findAll();

                    if (ServerInfoManager.servers.isEmpty())
                        ServerInfoManager.servers.addAll(serverInfoSet);
                    else {
                        serverInfoSet.forEach(serverInfo -> {
                            ServerInfo serverInfo1 = ServerInfoManager.getServerInfo(serverInfo.getId());

                            if (serverInfo1 == null) {
                                ServerInfoManager.servers.add(serverInfo);
                            } else {
                                if (!serverInfo.isSimilar(serverInfo1))
                                    serverInfo1.update(serverInfo);
                            }
                        });
                    }
                },
                0,
                30,
                TimeUnit.SECONDS
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
                null
        );
    }
}
