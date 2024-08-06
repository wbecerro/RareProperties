package wbe.rareproperties.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.legendary.Noctis;
import wbe.rareproperties.properties.legendary.Solem;
import wbe.rareproperties.properties.mythic.Fly;

import java.util.Set;

public class Scheduler {

    public static void startSchedulers(FileConfiguration config, RareProperties plugin) {
        startFlyCost(config, plugin);
        startSunTimeChecking(config, plugin);
        startMoonTimeChecking(config, plugin);
    }

    private static void startFlyCost(FileConfiguration config, RareProperties plugin) {
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

    private static void startSunTimeChecking(FileConfiguration config, RareProperties plugin) {
        World world = Bukkit.getServer().getWorld("world");
        double maxDamage = config.getDouble("Properties.Solem.damagePercent");

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                long time = world.getTime();
                if(time >= 0L && time <= 999L) {
                    Solem.power = maxDamage / 6;
                } else if(time >= 1000L && time <= 1999L) {
                    Solem.power = maxDamage / 5;
                } else if(time >= 2000L && time <= 2999L) {
                    Solem.power = maxDamage / 4;
                } else if(time >= 3000L && time <= 3999L) {
                    Solem.power = maxDamage / 3;
                }  else if(time >= 4000L && time <= 4999L) {
                    Solem.power = maxDamage / 2;
                } else if(time >= 5000L && time <= 6999L) {
                    Solem.power = maxDamage;
                }  else if(time >= 7000L && time <= 7999L) {
                    Solem.power = maxDamage / 2;
                }  else if(time >= 8000L && time <= 8999L) {
                    Solem.power = maxDamage / 3;
                }  else if(time >= 9000L && time <= 9999L) {
                    Solem.power = maxDamage / 4;
                }  else if(time >= 10000L && time <= 10999L) {
                    Solem.power = maxDamage / 5;
                }  else if(time >= 11000L && time <= 11999L) {
                    Solem.power = maxDamage / 6;
                }
            }
        }, 10L, 20L * 20L);
    }

    private static void startMoonTimeChecking(FileConfiguration config, RareProperties plugin) {
        World world = Bukkit.getServer().getWorld("world");
        double maxDamage = config.getDouble("Properties.Noctis.damagePercent");

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                long time = world.getTime();
                if(time >= 12000L && time <= 12999L) {
                    Noctis.power = maxDamage / 6;
                } else if(time >= 13000L && time <= 13999L) {
                    Noctis.power = maxDamage / 5;
                } else if(time >= 14000L && time <= 14999L) {
                    Noctis.power = maxDamage / 4;
                } else if(time >= 15000L && time <= 15999L) {
                    Noctis.power = maxDamage / 3;
                }  else if(time >= 16000L && time <= 16999L) {
                    Noctis.power = maxDamage / 2;
                } else if(time >= 17000L && time <= 18999L) {
                    Noctis.power = maxDamage;
                }  else if(time >= 19000L && time <= 19999L) {
                    Noctis.power = maxDamage / 2;
                }  else if(time >= 20000L && time <= 20999L) {
                    Noctis.power = maxDamage / 3;
                }  else if(time >= 21000L && time <= 21999L) {
                    Noctis.power = maxDamage / 4;
                }  else if(time >= 220000L && time <= 220999L) {
                    Noctis.power = maxDamage / 5;
                }  else if(time >= 23000L && time <= 23999L) {
                    Noctis.power = maxDamage / 6;
                }
            }
        }, 10L, 20L * 20L);
    }
}
