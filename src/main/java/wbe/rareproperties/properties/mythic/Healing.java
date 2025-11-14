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
        if(!applyFoodCost(player, cost)) {
            return;
        }

        double newHealth = player.getHealth() + (player.getAttribute(Attribute.MAX_HEALTH).getValue() *
                (RareProperties.propertyConfig.healingPercent * getLevel() / 100));
        double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();
        player.setHealth(Math.min(maxHealth, newHealth));
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = -1;

        if(player.getHealth() >= player.getAttribute(Attribute.MAX_HEALTH).getValue()) {
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
