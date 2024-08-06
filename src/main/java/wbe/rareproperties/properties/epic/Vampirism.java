package wbe.rareproperties.properties.epic;

import org.bukkit.Material;
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
        super(plugin, new ArrayList<>(), "vampirism", "Vampirismo");
        setDescription(getConfig().getStringList("Properties.Vampirism.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        double damage = ((EntityDamageByEntityEvent) event).getDamage();
        double healing = damage * (getLevel() * getConfig().getDouble("Properties.Vampirism.healthSteal"));
        double newHealth = player.getHealth() + healing;
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        if(newHealth >= maxHealth) {
            player.setHealth(maxHealth);
        } else {
            player.setHealth(newHealth);
        }
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if (!player.hasPermission("rareproperties.use.vampirism")) {
            return false;
        }

        if(player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            return false;
        }

        if (!utilities.hasProperty(player.getInventory().getItemInMainHand(), "Vampirismo")) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        level = checkProperty(item, "Vampirismo");

        if (level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
