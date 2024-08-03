package wbe.rareproperties.util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.Fly;

import java.util.Set;

public class Scheduler {

    public static void startFlyCost(FileConfiguration config, RareProperties plugin) {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                Set<Player> keys = Fly.playersFlying.keySet();
                for(Player player : keys) {
                    if(player.isFlying()) {
                        int level = Fly.playersFlying.get(player);
                        int rest = (int)(config.getDouble("Properties.Fly.baseCost") - level);
                        final int food = rest;
                        player.setFoodLevel(player.getFoodLevel() - food);
                    }

                    if(player.getFoodLevel() <= 0) {
                        player.setFlying(false);
                    }
                }
            }
        }, 10L, config.getLong("Properties.Fly.time") * 20);
    }
}
