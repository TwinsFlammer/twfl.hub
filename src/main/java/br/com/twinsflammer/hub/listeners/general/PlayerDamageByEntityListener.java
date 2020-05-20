package br.com.twinsflammer.hub.listeners.general;

import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by @SrGutyerrez
 */
public class PlayerDamageByEntityListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) event.setCancelled(true);

        if (entity instanceof ItemFrame)
            event.setCancelled(true);
    }
}
