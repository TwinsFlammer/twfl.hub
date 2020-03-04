package com.redecommunity.hub.selector.item;

import com.redecommunity.api.spigot.inventory.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by @SrGutyerrez
 */
public class SelectorItem extends CustomItem {
    public SelectorItem() {
        super(Material.AIR);
    }

    @Override
    public ItemStack build() {
        return new CustomItem(Material.COMPASS)
                .name("§bSelecionar Servidor")
                .lore("§7Clique com direito para escolher o servidor que deseja jogar.")
                .build();
    }
}
