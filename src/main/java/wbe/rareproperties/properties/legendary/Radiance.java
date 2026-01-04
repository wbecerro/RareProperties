package wbe.rareproperties.properties.legendary;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Radiance extends RareProperty {

    public Radiance(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "radiance", RareProperties.propertyConfig.radianceName);
        setDescription(RareProperties.propertyConfig.radianceDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        double damage = ((EntityDamageByEntityEvent) event).getDamage();
        Bukkit.broadcast("Daño base: " + damage, "*");
        double percent = 1 + getLevel() * RareProperties.propertyConfig.radianceExtraDamage;
        Bukkit.broadcast("Porcentaje extra: " + percent, "*");

        damage = damage * percent;
        Bukkit.broadcast("Daño final: " + damage, "*");
        ((EntityDamageByEntityEvent) event).setDamage(damage);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        level = checkProperty(item, getExternalName());

        if(level < 0) {
            return false;
        }

        if(player.getWorld().hasStorm()) {
            Bukkit.broadcast("Hay tormenta", "*");
            setLevel(level);
            return true;
        }

        return false;
    }
}
