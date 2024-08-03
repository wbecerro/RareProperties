package wbe.rareproperties.properties;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;

import java.util.ArrayList;

public class Healing extends RareProperty {

    public Healing(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "healing", "Sanación");
        setDescription(getConfig().getStringList("Properties.Healing.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        int cost = getConfig().getInt("Properties.Healing.foodCost");

        double newHealth = player.getHealth() * (1 + getConfig().getDouble("Properties.Healing.healthPercent") / 100);
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        if(newHealth > maxHealth) {
            newHealth = maxHealth;
        }
        player.setHealth(newHealth);

        player.setFoodLevel(player.getFoodLevel() - cost);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if(!player.hasPermission("rareproperties.use.healing")) {
            return false;
        }

        if(player.getFoodLevel() < getConfig().getInt("Properties.Healing.foodCost")) {
            return false;
        }

        if(player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        level = checkHands(inventory, "Sanación");

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
