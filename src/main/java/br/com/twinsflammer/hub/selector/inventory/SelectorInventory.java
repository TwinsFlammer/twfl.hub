package br.com.twinsflammer.hub.selector.inventory;

import br.com.twinsflammer.hub.selector.exception.UnknownServerException;
import br.com.twinsflammer.api.spigot.inventory.CustomPaginateInventory;
import br.com.twinsflammer.api.spigot.inventory.item.CustomItem;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.server.data.Server;
import br.com.twinsflammer.common.shared.server.enums.ServerType;
import br.com.twinsflammer.common.shared.server.manager.ServerManager;
import br.com.twinsflammer.hub.selector.manager.ServerInfoManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.Comparator;
import java.util.Objects;

/**
 * Created by @SrGutyerrez
 */
public class SelectorInventory extends CustomPaginateInventory {
    public SelectorInventory() {
        super(
                "Selecione o servidor",
                3
        );

        this.setCancelled(true);

        ServerInfoManager.getServers()
                .forEach(serverInfo -> {
                    try {
                        CustomItem customItem = serverInfo.getCustomItem();

                        this.setItem(
                                serverInfo.getSlot(),
                                customItem
                        );
                    } catch (UnknownServerException exception) {
                        exception.printStackTrace();
                    }
                });
    }

    public SelectorInventory(ServerType serverType, User user) {
        super(
                "Selecione o " + serverType.getCategoryName(),
                3,
                "XXXXXXXXX",
                "XOXXOXXOX",
                "XXXXXXXXX"
        );

        this.setCancelled(true);

        ServerManager.getServers()
                .stream()
                .filter(Objects::nonNull)
                .filter(server -> server.getType().equals(serverType))
                .sorted(Comparator.comparing(Server::getName))
                .forEach(server -> {
                    Server userServer = user.getServer();

                    CustomItem customItem = new CustomItem(Material.INK_SACK)
                            .data(userServer.isSimilar(server) ? 13 : server.isOnline() ? 10 : 8)
                            .name((userServer.isSimilar(server) ? "§e" : server.isOnline() ? "§a" : "§c") + ChatColor.stripColor(server.getDisplayName()))
                            .lore(
                                    String.format(
                                            "§fJogadores: §7%d/%d",
                                            server.getPlayerCount(),
                                            server.getSlots()
                                    ),
                                    "",
                                    userServer.isSimilar(server) ? "§eVocê já está conectado a este saguão!" : server.isOnline() ? "§aClique para conetar!" : "§cEste saguão está offline."
                            );

                    if (userServer.isSimilar(server))
                        customItem.enchant(Enchantment.DURABILITY, 1);

                    customItem.hideAttributes()
                            .onClick(event -> {
                                if (userServer.isSimilar(server)) return;

                                if (server.isOnline()) user.connect(server);
                            });

                    this.addItem(customItem);
                });
    }
}
