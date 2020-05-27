package br.com.twinsflammer.hub.listeners.general;

import br.com.twinsflammer.api.spigot.util.Cuboid;
import br.com.twinsflammer.api.spigot.util.jsontext.data.JSONText;
import br.com.twinsflammer.common.shared.Common;
import br.com.twinsflammer.common.shared.cooldown.manager.CooldownManager;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
public class PlayerInteractAtEntityListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        User user = UserManager.getUser(player.getUniqueId());

        Entity entity = event.getRightClicked();

        if (entity.getType() == EntityType.ITEM_FRAME) {
            Location location = entity.getLocation();

            Integer x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();

            Cuboid cuboid = new Cuboid(
                    -4,
                    46,
                    -37,
                    4,
                    51,
                    -39,
                    player.getWorld()
            );

            if (cuboid.contains(x, y, z)) {
                if (CooldownManager.inCooldown(user, "HUB_ITEM_FRAME")) return;

                new JSONText()
                        .text("\n")
                        .next()
                        .text("  §aClique ")
                        .next()
                        .text("§a§lAQUI")
                        .clickOpenURL(Common.SERVER_URL)
                        .next()
                        .text(" §r§apara acessar o site!")
                        .next()
                        .text("\n")
                        .next()
                        .send(player);

                CooldownManager.startCooldown(
                        user,
                        TimeUnit.SECONDS.toMillis(1),
                        "HUB_ITEM_FRAME"
                );
            }
        }

        if (user.hasGroup(GroupNames.MANAGER)) return;

        event.setCancelled(true);
    }
}
