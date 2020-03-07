package com.redecommunity.hub.scoreboard.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.redecommunity.api.shared.connection.manager.ProxyServerManager;
import com.redecommunity.api.spigot.scoreboard.CustomBoard;
import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.group.data.Group;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.common.shared.server.manager.ServerManager;
import com.redecommunity.hub.Hub;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by @SrGutyerrez
 */
public class ScoreboardManager {
    @Getter
    private static HashMap<Integer, CustomBoard> customBoards = Maps.newHashMap();

    private Queue<Player> players = Lists.newLinkedList(Bukkit.getOnlinePlayers());

    public ScoreboardManager() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(
                Hub.getInstance(),
                () -> {
                    Player player = this.players.poll();

                    if (player != null) {
                        try {
                            User user = UserManager.getUser(player.getUniqueId());

                            ScoreboardManager.update(user);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }

                    if (this.players.isEmpty())
                        this.players = Lists.newLinkedList(Bukkit.getOnlinePlayers());
                },
                0,
                20L*5
        );
    }

    public static CustomBoard create(User user) {
        Integer playerCount = ProxyServerManager.getProxyCountOnline();

        CustomBoard customBoard = new CustomBoard();

        customBoard.title("§c§lREDE COMMUNITY")
                .set(15, "§0")
                .set(14, " §fOnline §c» §f" + playerCount)
                .set(13, "§1")
                .set(12, "  §fFactions");

        ScoreboardManager.setServerList(customBoard);

        String prefix = ScoreboardManager.getPrefix(user);

        customBoard.set(3, "§2")
                .set(2, " §fGrupo §c» §f" + prefix)
                .set(1, "§3")
                .set(0, "§c   redecommunity.com");

        ScoreboardManager.getCustomBoards().put(
                user.getId(),
                customBoard
        );

        return customBoard;
    }

    public static CustomBoard update(User user) {
        Integer playerCount = ProxyServerManager.getUsers().size();

        CustomBoard customBoard = ScoreboardManager.customBoards.get(user.getId());

        String prefix = ScoreboardManager.getPrefix(user);

        customBoard.set(14, " §fOnline §c» §f" + playerCount)
                .set(2, " §fGrupo §c» §f" + prefix);

        ScoreboardManager.setServerList(customBoard);


        return customBoard;
    }

    private static String getPrefix(User user) {
        Group group = user.getHighestGroup();

        return group.getName().equals(GroupNames.DEFAULT) ? "§7Membro" : group.getFancyPrefix();
    }

    private static void setServerList(CustomBoard customBoard) {
        List<Server> servers = ServerManager.getServers()
                .stream()
                .filter(Server::isFactions)
                .collect(Collectors.toList());

        AtomicInteger score = new AtomicInteger(11);

        IntStream.range(0, servers.size())
                .forEach(index -> {
                    Server server = servers.get(index);

                    String fancyName = StringUtils.replaceEach(
                            server.getDisplayName(),
                            new String[]{
                                    "Factions"
                            },
                            new String[]{
                                    "F."
                            }
                    );

                    String statusString =  server.getStatusString();

                    customBoard.set(score.get(), "    §f" + fancyName + " §b» §f" + statusString);

                    score.getAndIncrement();
                });
    }
}
