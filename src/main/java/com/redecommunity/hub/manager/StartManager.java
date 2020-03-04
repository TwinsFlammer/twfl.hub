package com.redecommunity.hub.manager;

import com.redecommunity.api.spigot.commands.CustomCommand;
import com.redecommunity.api.spigot.commands.registry.CommandRegistry;
import com.redecommunity.common.shared.databases.mysql.dao.Table;
import com.redecommunity.common.shared.util.ClassGetter;
import com.redecommunity.hub.Hub;
import com.redecommunity.hub.scoreboard.manager.ScoreboardManager;
import com.redecommunity.hub.selector.manager.ServerInfoManager;
import com.redecommunity.hub.spawn.manager.SpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * Created by @SrGutyerrez
 */
public class StartManager {
    public StartManager() {
        new ListenerManager();

        new CommandManager();

        new TableManager();

        new DataManager();
    }
}

class ListenerManager {
    ListenerManager() {
        ClassGetter.getClassesForPackage(Hub.class).forEach(clazz -> {
            if (Listener.class.isAssignableFrom(clazz)) {
                try {
                    Listener listener = (Listener) clazz.newInstance();

                    Bukkit.getPluginManager().registerEvents(
                            listener,
                            Hub.getInstance()
                    );
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

class CommandManager {
    CommandManager() {
        ClassGetter.getClassesForPackage(Hub.class).forEach(clazz -> {
            if (CustomCommand.class.isAssignableFrom(clazz)) {
                try {
                    CustomCommand customCommand = (CustomCommand) clazz.newInstance();

                    CommandRegistry.registerCommand(
                            Hub.getInstance(),
                            customCommand
                    );
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

class TableManager {
    TableManager() {
        ClassGetter.getClassesForPackage(Hub.class).forEach(clazz -> {
            if (Table.class.isAssignableFrom(clazz)) {
                try {
                    Table table = (Table) clazz.newInstance();

                    table.createTable();
                } catch (IllegalAccessException | InstantiationException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

class DataManager {
    DataManager() {
        new ScoreboardManager();

        new SpawnManager();

        new ServerInfoManager();
    }
}