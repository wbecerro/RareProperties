package wbe.rareproperties.properties.mythic;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.*;

public class Restoration extends RareProperty {

    public static Set<Player> playersRestoration = new HashSet<>();

    public Restoration(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "restoration", RareProperties.propertyConfig.restorationName);
        setDescription(RareProperties.propertyConfig.restorationDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        playersRestoration.add(player);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = -1;

        PlayerInventory inventory = player.getInventory();
        level = checkArmor(inventory, getExternalName());

        if(level < 0) {
            level = checkHands(inventory, getExternalName());
            if(level < 0) {
                Restoration.playersRestoration.remove(player);
                return false;
            }
        }

        setLevel(level);
        return true;
    }
}
