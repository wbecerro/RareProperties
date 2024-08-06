package wbe.rareproperties.properties.mythic;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.HashMap;

public class Fly extends RareProperty {

    public static HashMap<Player, Integer> playersFlying = new HashMap<Player, Integer>();

    public Fly(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "fly", "Vuelo");
        setDescription(getConfig().getStringList("Properties.Fly.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        if (player.getFoodLevel() <= 0) {
            player.setAllowFlight(false);
        } else {
            player.setAllowFlight(true);
            player.setFlySpeed(0.1F);
        }

        playersFlying.put(player, getLevel());
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            Fly.playersFlying.remove(player);
            return false;
        }

        if(!player.hasPermission("rareproperties.use.fly")) {
            Fly.playersFlying.remove(player);
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        level = checkArmor(inventory, "Vuelo");

        if(level < 0) {
            level = checkHands(inventory, "Vuelo");
        }

        if(level < 0) {
            Fly.playersFlying.remove(player);
            player.setAllowFlight(false);
            return false;
        }

        setLevel(level);
        return true;
    }
}
