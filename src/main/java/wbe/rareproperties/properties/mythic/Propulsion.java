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
        super(plugin, new ArrayList<>(), "propulsion", RareProperties.propertyConfig.propulsionName);
        setDescription(RareProperties.propertyConfig.propulsionDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        int cost = RareProperties.propertyConfig.propulsionCost;

        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1F, 1F);
        player.setVelocity(new Vector(0, RareProperties.propertyConfig.propulsionImpulse * getLevel(), 0));

        player.setFoodLevel(player.getFoodLevel() - cost);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = -1;

        if(player.getFoodLevel() < RareProperties.propertyConfig.propulsionCost) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        level = checkHands(inventory, getExternalName());

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
