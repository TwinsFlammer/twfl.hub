package com.redecommunity.hub.selector.inventory;

import com.redecommunity.api.spigot.inventory.CustomPaginateInventory;
import com.redecommunity.api.spigot.inventory.item.CustomItem;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.common.shared.server.enums.ServerType;
import com.redecommunity.common.shared.server.manager.ServerManager;
import com.redecommunity.hub.selector.exception.UnknownServerException;
import com.redecommunity.hub.selector.manager.ServerInfoManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

/**
 * Created by @SrGutyerrez
 */
public class SelectorInventory extends CustomPaginateInventory {
    public SelectorInventory() {
        super(
                "Selecione o servidor",
                3
        );

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

        ServerManager.getServers()
                .stream()
                .filter(Server::isLobby)
                .forEach(server -> {
                    Server userServer = user.getServer();

                    CustomItem customItem = new CustomItem(Material.INK_SACK)
                            .data(userServer.isSimilar(server) ? 13 : server.isOnline() ? 10 : 8)
                            .name((userServer.isSimilar(server) ? "§e" : server.isOnline() ? "§a" : "§c" ) + ChatColor.stripColor(server.getDisplayName()))
                            .lore(
                                    String.format(
                                            "§fJogadores: §7%d/%d",
                                            server.getPlayerCount(),
                                            server.getSlots()
                                    ),
                                    "",
                                    userServer.isSimilar(server) ? "§eVocê já está conectado a este saguão!" : server.isOnline() ? "§aClique para conetar!" : "§cEste saguão está offline."
                            )
                            .enchant(userServer.isSimilar(server) ? Enchantment.DURABILITY : null, 1)
                            .hideAttributes()
                            .onClick(event -> {
                                if (userServer.isSimilar(server)) return;

                                if (server.isOnline()) user.connect(server);
                            });

                    this.addItem(customItem);
                });
    }
}
