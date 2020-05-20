package br.com.twinsflammer.hub.manager;

import br.com.twinsflammer.hub.selector.manager.ServerInfoManager;
import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.registry.CommandRegistry;
import br.com.twinsflammer.common.shared.databases.mysql.dao.Table;
import br.com.twinsflammer.common.shared.util.ClassGetter;
import br.com.twinsflammer.hub.Hub;
import br.com.twinsflammer.hub.queue.manager.QueueManager;
import br.com.twinsflammer.hub.scoreboard.manager.ScoreboardManager;
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

        new ServerInfoManager();

        new QueueManager();
    }
}