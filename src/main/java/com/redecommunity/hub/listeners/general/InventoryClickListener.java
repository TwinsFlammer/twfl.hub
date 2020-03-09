package com.redecommunity.hub.listeners.general;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by @SrGutyerrez
 */
public class InventoryClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory instanceof PlayerInventory)
            event.setCancelled(true);
    }
}
