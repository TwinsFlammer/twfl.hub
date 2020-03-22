package com.redecommunity.hub.item.enums;

import com.redecommunity.api.spigot.inventory.item.CustomItem;
import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.preference.Preference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;

/**
 * Created by @SrGutyerrez
 */
@RequiredArgsConstructor
@Getter
public enum LobbyItem {
    VISIBILITY_ON(
            1,
            new CustomItem(Material.SLIME_BALL)
                    .name("§aVisibilidade dos jogadores")
                    .hideAttributes()
                    .build()
    ),
    VISIBILITY_OFF(
            1,
            new CustomItem(Material.FIREWORK_CHARGE)
                    .name("§cVisibilidade dos jogadores")
                    .hideAttributes()
                    .build()
    ),
    SERVER_SELECTOR(
            4,
            new CustomItem(Material.COMPASS)
                    .name("§bSelecionar Servidor")
                    .lore("§7Clique com direito para escolher", "§7o servidor que deseja jogar.")
                    .hideAttributes()
                    .build()
    ),
    LOBBY(
            7,
            new CustomItem(Material.NETHER_STAR)
                    .name("§aSaguões")
                    .hideAttributes()
                    .build()
    );

    private final Integer slot;
    private final ItemStack icon;

    public LobbyItem getParent() {
        switch (this) {
            case VISIBILITY_OFF:
                return VISIBILITY_ON;
            case VISIBILITY_ON:
                return VISIBILITY_OFF;
        }

        return null;
    }

    public void give(Player player) {
        User user = UserManager.getUser(player.getUniqueId());

        PlayerInventory playerInventory = player.getInventory();

        if (this == LobbyItem.VISIBILITY_ON && user.isDisabled(Preference.VISIBILITY)
                || this == LobbyItem.VISIBILITY_OFF && user.isEnabled(Preference.VISIBILITY)) return;

        playerInventory.setItem(
                this.slot,
                this.icon
        );

        player.updateInventory();

        if (this == LobbyItem.VISIBILITY_ON || this == LobbyItem.VISIBILITY_OFF) {
            Preference preference = Preference.VISIBILITY;

            Boolean enabled = user.isEnabled(preference);

            Bukkit.getOnlinePlayers().forEach(player1 -> {
                User user1 = UserManager.getUser(player1.getUniqueId());

                if (!user1.hasGroup(GroupNames.HELPER)) {
                    if (enabled) {
                        player.showPlayer(player1);
                    } else {
                        player.hidePlayer(player1);
                    }
                }
            });
        }
    }

    public static LobbyItem getLobbyItem(ItemStack itemStack) {
        return Arrays.stream(LobbyItem.values())
                .filter(lobbyItem -> lobbyItem.getIcon().isSimilar(itemStack))
                .findFirst()
                .orElse(null);
    }
}
