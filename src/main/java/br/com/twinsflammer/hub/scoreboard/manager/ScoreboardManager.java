package br.com.twinsflammer.hub.scoreboard.manager;

import br.com.twinsflammer.hub.Hub;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import br.com.twinsflammer.api.shared.connection.manager.ProxyServerManager;
import br.com.twinsflammer.api.spigot.scoreboard.CustomBoard;
import br.com.twinsflammer.common.shared.Common;
import br.com.twinsflammer.common.shared.permissions.group.data.Group;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.server.data.Server;
import br.com.twinsflammer.common.shared.server.manager.ServerManager;
import br.com.twinsflammer.common.shared.util.Helper;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        customBoard.title("§c§lTWINS FLAMMER")
                .set(15, "§0")
                .set(14, " §fOnline: §c" + playerCount)
                .set(13, "§1")
                .set(12, " §fFactions");

        ScoreboardManager.setServerList(customBoard);

        String prefix = ScoreboardManager.getPrefix(user);

        customBoard.set(3, "§2")
                .set(2, " §fGrupo: " + prefix)
                .set(1, "§3")
                .set(0, "§c  " + Common.SERVER_URL);

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

        customBoard.set(14, " §fOnline: §c" + playerCount)
                .set(2, " §fGrupo: " + prefix);

        ScoreboardManager.setServerList(customBoard);

        return customBoard;
    }

    private static String getPrefix(User user) {
        Group group = user.getHighestGroup();

        return group.isDefault() ? "§7Membro" : group.getFancyPrefix();
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

                    String statusString = server.isOnline() ? server.getPlayerCount().toString() : (server.getStatusString().contains(" ") ? ChatColor.getLastColors(server.getStatusString()) + Helper.capitalize(server.getStatusString().split(" ")[1]) : server.getStatusString()).substring(0, 5);

                    customBoard.set(score.get(), "   §f" + fancyName + ": §b" + statusString);

                    score.getAndIncrement();
                });
    }
}
