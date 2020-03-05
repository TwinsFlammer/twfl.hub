package com.redecommunity.hub.selector.data;

import com.redecommunity.api.spigot.hologram.data.CustomHologram;
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
import net.citizensnpcs.api.npc.NPC;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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

    public void update(ServerInfo serverInfo) {
        this.npcId = serverInfo.npcId;
        this.slot = serverInfo.slot;
        this.serverId = serverInfo.serverId;
        this.active = serverInfo.active;
        this.material = serverInfo.material;
        this.description = serverInfo.description;
    }

    public void spawn(NPC npc) {
        Location location = this.getHologramLocation(npc);

        Server server = this.getServer();

        CustomHologram customHologram = new CustomHologram(location);

        customHologram.appendLines(
                "§e" + server.getDisplayName(),
                "§b" + server.getPlayerCount() + "/" + server.getSlots()
        );

        customHologram.spawn();

        this.hologram = customHologram;
    }

    public void despawn() {
        this.hologram.despawn();
    }

    public void updateHologram() {
        CustomHologram customHologram = this.hologram;

        Server server = this.getServer();

        System.out.println("atualiza ai tio");

        customHologram.updateLines(
                "§e" + server.getDisplayName(),
                "§b" + server.getPlayerCount() + "/" + server.getSlots()
        );
    }

    public void teleportHologram(NPC npc) {
        Location location = this.getHologramLocation(npc);

        CustomHologram customHologram = this.hologram;

        customHologram.teleport(location);
    }

    private Location getHologramLocation(NPC npc) {
        Entity npcEntity = npc.getEntity();

        if (npcEntity.getType() == EntityType.PLAYER) {
            Player player = (Player) npcEntity;

            return player.getLocation().clone().add(0,0.7,0);
        } else return npcEntity.getLocation().clone().add(0,0.7,0);
    }
}
