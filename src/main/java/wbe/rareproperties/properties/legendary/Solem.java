package wbe.rareproperties.properties.legendary;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.Random;

public class Solem extends RareProperty {

    public static double power = 0.0;

    public Solem(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "solem", "Solem");
        setDescription(getConfig().getStringList("Properties.Solem.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        double damage = ((EntityDamageByEntityEvent) event).getDamage();
        damage = damage * (1 + power * getLevel());
        ((EntityDamageByEntityEvent) event).setDamage(damage);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if (!player.hasPermission("rareproperties.use.solem")) {
            return false;
        }

        if(player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            return false;
        } else if(player.getInventory().getItemInMainHand().getItemMeta() == null) {
            return false;
        }

        if (!utilities.hasProperty(player.getInventory().getItemInMainHand(), "Solem")) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        level = checkProperty(item, "Solem");

        if (level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
