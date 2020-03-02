package com.redecommunity.hub.selector.data;

import com.redecommunity.api.spigot.inventory.item.CustomItem;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.common.shared.server.manager.ServerManager;
import com.redecommunity.hub.selector.exception.UnknownServerException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by @SrGutyerrez
 */
@AllArgsConstructor
public class ServerInfo {
    @Getter
    private final Integer id;

    @Getter
    @Setter
    private Integer npcId;

    @Getter
    private final Integer slot, serverId;

    private Boolean active;

    @Setter
    private Material material;

    @Getter
    private String description;

    public Boolean isActive() {
        return this.active;
    }

    public CustomItem getCustomItem() throws UnknownServerException {
        CustomItem customItem = new CustomItem(this.material);

        Server server = ServerManager.getServer(this.serverId);

        if (server == null)
            throw new UnknownServerException("Não foi possível localizar esse servidor.");

        String[] lore = this.description.split("\\\\n");

        customItem.name("§a" + server.getDisplayName())
                .lore(lore)
                .hideAttributes()
                .cancelled(true)
                .onClick(event -> {
                    Player player = (Player) event.getWhoClicked();

                    User user = UserManager.getUser(player.getUniqueId());

                    Language language = user.getLanguage();

                    if (server.inBetaVip() && !user.isVIP()) {
                        player.sendMessage(
                                language.getMessage("server.beta_vip")
                        );
                        return;
                    }

                    if (server.getPlayerCount() >= server.getSlots() && !user.isVIP()) {
                        // adicionar a fila para conseguir entrar no servidor
                    } else {
                        user.connect(server);
                    }
                });

        return customItem;
    }
}
