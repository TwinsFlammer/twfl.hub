package com.redecommunity.hub.selector.data;

import com.redecommunity.api.spigot.hologram.CustomHologram;
import com.redecommunity.api.spigot.inventory.item.CustomItem;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.common.shared.server.manager.ServerManager;
import com.redecommunity.hub.selector.exception.UnknownServerException;
import com.redecommunity.hub.selector.manager.ServerInfoManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.citizensnpcs.api.npc.NPC;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
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
    private Integer npcId, slot, serverId;

    private Boolean active;

    @Setter
    private Material material;

    @Getter
    private String description;

    @Getter
    @Setter
    private CustomHologram hologram;

    public Server getServer() {
        return ServerManager.getServer(this.serverId);
    }

    public Boolean isActive() {
        return this.active;
    }

    public Boolean isSimilar(ServerInfo serverInfo) {
        return serverInfo.npcId.equals(this.npcId)
                && serverInfo.slot.equals(this.slot)
                && serverInfo.serverId.equals(this.serverId)
                && serverInfo.active.equals(this.active)
                && serverInfo.material.equals(this.material)
                && serverInfo.description.equals(this.description);
    }

    public CustomItem getCustomItem() throws UnknownServerException {
        CustomItem customItem = new CustomItem(this.material);

        Server server = ServerManager.getServer(this.serverId);

        if (server == null)
            throw new UnknownServerException("Não foi possível localizar esse servidor.");

        String[] lore = StringUtils.replaceEach(
                this.description,
                new String[]{
                        "{online}",
                        "{slots}",
                        "{status}"
                },
                new String[]{
                        server.getPlayerCount().toString(),
                        server.getSlots().toString(),
                        server.getStatusString()
                }
        ).split("\\\\n");

        customItem.name("§a" + server.getDisplayName())
                .lore(lore)
                .hideAttributes()
                .cancelled(true)
                .onClick(event -> {
                    Player player = (Player) event.getWhoClicked();

                    this.connect(player);
                });

        return customItem;
    }

    public void connect(Player player) {
        User user = UserManager.getUser(player.getUniqueId());

        Language language = user.getLanguage();

        Server server = ServerManager.getServer(this.serverId);

        if (!server.isOnline()) {
            player.sendMessage(
                    language.getMessage("messages.default_commands.server.offline_server")
            );
            return;
        }

        if (server.inBetaVip() && !user.isVIP()) {
            player.sendMessage(
                    language.getMessage("messages.default_commands.server.beta_vip")
            );
            return;
        }

        if (server.getPlayerCount() >= server.getSlots() && !user.isVIP()) {
            // adicionar a fila para conseguir entrar no servidor
        } else {
            user.connect(server);
        }
    }

    public void update(ServerInfo serverInfo) {
        this.npcId = serverInfo.npcId;
        this.slot = serverInfo.slot;
        this.serverId = serverInfo.serverId;
        this.active = serverInfo.active;
        this.material = serverInfo.material;
        this.description = serverInfo.description;
    }

    public void spawn() {
        Location location = this.getHologramLocation();

        Server server = this.getServer();

        CustomHologram customHologram = new CustomHologram(location);

        customHologram.spawn();

        customHologram.appendLines(
                server.getStatus() == 0 && !server.isOnline() ? server.getStatusString() : "§b" + server.getPlayerCount() + "/" + server.getSlots(),
                "§e" + server.getDisplayName(),
                server.getStatus() == 0 ? null : server.getStatusString()
        );

        this.hologram = customHologram;
    }

    public void despawn() {
        this.hologram.despawn();
    }

    public void updateHologram() {
        CustomHologram customHologram = this.hologram;

        Server server = this.getServer();

        customHologram.updateLines(
                server.getStatus() == 0 && !server.isOnline() ? server.getStatusString() : "§b" + server.getPlayerCount() + "/" + server.getSlots(),
                "§e" + server.getDisplayName(),
                server.getStatus() == 0 ? null : server.getStatusString()
        );
    }

    public void teleportHologram() {
        Location location = this.getHologramLocation();

        CustomHologram customHologram = this.hologram;

        customHologram.teleport(location);
    }

    private Location getHologramLocation() {
        NPC npc = ServerInfoManager.getNPC(this);

        return npc.getStoredLocation().clone().add(0, 0.7, 0);
    }
}
