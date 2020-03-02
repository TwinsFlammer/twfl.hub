package com.redecommunity.hub.spawn.command;

import com.redecommunity.hub.spawn.manager.SpawnManager;
import com.redecommunity.hub.spawn.storage.SpawnStorage;
import com.redecommunity.api.spigot.commands.CustomCommand;
import com.redecommunity.api.spigot.commands.enums.CommandRestriction;
import com.redecommunity.api.spigot.util.serialize.LocationSerialize;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.user.data.User;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by @SrGutyerrez
 */
public class SetSpawnCommand extends CustomCommand {
    public SetSpawnCommand() {
        super("setspawn", CommandRestriction.IN_GAME, GroupNames.MANAGER);
    }

    @Override
    public void onCommand(CommandSender sender, User user, String[] args) {
        Language language = user.getLanguage();

        Player player = (Player) sender;

        Location location = player.getLocation();

        String serializedLocation = LocationSerialize.toString(location);

        SpawnStorage spawnStorage = new SpawnStorage();

        SpawnManager.DEFAULT_SPAWN = location;

        spawnStorage.update(
                "location",
                serializedLocation
        );

        sender.sendMessage(
                language.getMessage("spawn.changed")
        );
    }
}
