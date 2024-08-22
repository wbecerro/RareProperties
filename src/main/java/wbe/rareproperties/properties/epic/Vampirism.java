package wbe.rareproperties.properties.epic;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Vampirism extends RareProperty {

    public Vampirism(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "vampirism", RareProperties.propertyConfig.vampirismName);
        setDescription(RareProperties.propertyConfig.vampirismDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        double damage = ((EntityDamageByEntityEvent) event).getDamage();
        double healing = damage * (getLevel() * RareProperties.propertyConfig.vampirismHealth);
        double newHealth = player.getHealth() + healing;
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        player.setHealth(Math.min(newHealth, maxHealth));
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if(player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        level = checkProperty(item, getExternalName());

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
