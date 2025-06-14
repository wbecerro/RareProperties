package wbe.rareproperties.properties.mythic;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Healing extends RareProperty {

    public Healing(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "healing", RareProperties.propertyConfig.healingName);
        setDescription(RareProperties.propertyConfig.healingDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        int cost = RareProperties.propertyConfig.healingCost;

        double newHealth = player.getHealth() + (player.getAttribute(Attribute.MAX_HEALTH).getValue() *
                (RareProperties.propertyConfig.healingPercent * getLevel() / 100));
        double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();
        if(newHealth > maxHealth) {
            newHealth = maxHealth;
        }
        player.setHealth(newHealth);

        player.setFoodLevel(player.getFoodLevel() - cost);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = -1;

        if(player.getFoodLevel() < RareProperties.propertyConfig.healingCost) {
            return false;
        }

        if(player.getHealth() == player.getAttribute(Attribute.MAX_HEALTH).getValue()) {
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
