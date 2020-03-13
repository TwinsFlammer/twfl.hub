package com.redecommunity.hub.listeners.general;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import java.util.Arrays;

/**
 * Created by @SrGutyerrez
 */
public class BlockPhysicsListener implements Listener {
    private final Material[] materials = new Material[]{
            Material.SAPLING,
            Material.SAND,
            Material.ANVIL,
            Material.GRAVEL
    };

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();

        Material material = block.getType();

        if (Arrays.asList(materials).contains(material))
            event.setCancelled(true);
    }
}
