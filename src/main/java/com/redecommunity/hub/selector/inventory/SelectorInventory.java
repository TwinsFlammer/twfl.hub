package com.redecommunity.hub.selector.inventory;

import com.redecommunity.api.spigot.inventory.CustomPaginateInventory;
import com.redecommunity.api.spigot.inventory.item.CustomItem;
import com.redecommunity.hub.selector.exception.UnknownServerException;
import com.redecommunity.hub.selector.manager.ServerInfoManager;

/**
 * Created by @SrGutyerrez
 */
public class SelectorInventory extends CustomPaginateInventory {
    public SelectorInventory() {
        super(
                "Selecione o servidor",
                3
        );

        ServerInfoManager.getServers()
                .forEach(serverInfo -> {
                    try {
                        CustomItem customItem = serverInfo.getCustomItem();

                        this.setItem(
                                serverInfo.getSlot(),
                                customItem
                        );
                    } catch (UnknownServerException exception) {
                        exception.printStackTrace();
                    }
                });
    }
}
