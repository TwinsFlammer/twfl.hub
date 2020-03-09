package com.redecommunity.hub.item.manager;

import com.google.common.collect.Lists;
import com.redecommunity.common.shared.Common;
import com.redecommunity.common.shared.cooldown.data.Cooldown;
import com.redecommunity.common.shared.cooldown.manager.CooldownManager;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
public class LobbyItemManager {
    public LobbyItemManager() {
        List<Integer> started = Lists.newArrayList();

        Common.getInstance().getScheduler().scheduleAtFixedRate(
                () -> {
                    CooldownManager.getCooldowns().stream()
                            .filter(Cooldown::inCooldown)
                            .forEach(cooldown -> {
                                User user = UserManager.getUser(cooldown.getUserId());

                                Player player = Bukkit.getPlayer(user.getUniqueId());

                                if (!started.contains(user.getId())) {
                                    player.setTotalExperience(28);

                                    started.add(user.getId());
                                }

                                Integer newLevel = player.getTotalExperience() - 1;

                                if (newLevel <= 0)
                                    started.remove(user.getId());

                                player.setTotalExperience(
                                        newLevel
                                );
                            });
                },
                0,
                1,
                TimeUnit.MILLISECONDS
        );
    }
}
