package br.com.twinsflammer.hub.listeners.general;

import br.com.twinsflammer.api.spigot.util.Cuboid;
import br.com.twinsflammer.api.spigot.util.jsontext.data.JSONText;
import br.com.twinsflammer.common.shared.Common;
import br.com.twinsflammer.common.shared.cooldown.manager.CooldownManager;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import java.util.concurrent.TimeUnit;

/**
 * @author SrGutyerrez
 */
public class HangingBreakByEntityListener implements Listener {
    @EventHandler
    public void onHanging(HangingBreakByEntityEvent event) {
        Entity remover = event.getRemover(),
                entity = event.getEntity();

        if (entity.getType() == EntityType.ITEM_FRAME) {
            event.setCancelled(true);
        }

        if (remover.getType() == EntityType.PLAYER) {
            Player player = (Player) remover;

            User user = UserManager.getUser(player.getUniqueId());

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
    }
}
