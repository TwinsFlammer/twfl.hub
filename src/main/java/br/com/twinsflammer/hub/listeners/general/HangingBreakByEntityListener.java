package br.com.twinsflammer.hub.listeners.general;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

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
    }
}
