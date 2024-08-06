package wbe.rareproperties.properties.mythic;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Propulsion extends RareProperty {

    public Propulsion(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "propulsion", "Propulsión");
        setDescription(getConfig().getStringList("Properties.Propulsion.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        int cost = getConfig().getInt("Properties.Propulsion.foodCost");

        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1F, 1F);
        player.setVelocity(new Vector(0, getConfig().getInt("Properties.Propulsion.baseImpulse") * getLevel(), 0));

        player.setFoodLevel(player.getFoodLevel() - cost);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if(!player.hasPermission("rareproperties.use.propulsion")) {
            return false;
        }

        if(player.getFoodLevel() < getConfig().getInt("Properties.Propulsion.foodCost")) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        level = checkHands(inventory, "Propulsión");

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
