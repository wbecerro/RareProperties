package wbe.rareproperties.properties.epic;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Backstab extends RareProperty {

    public Backstab(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "backstab", RareProperties.propertyConfig.backstabName);
        setDescription(RareProperties.propertyConfig.backstabDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        double damage = ((EntityDamageByEntityEvent) event).getDamage();
        LivingEntity damaged = (LivingEntity) ((EntityDamageByEntityEvent) event).getEntity();

        if(damaged.getLocation().getDirection().dot(player.getLocation().getDirection()) > 0.0D) {
            damage = damage * (1 + RareProperties.propertyConfig.backstabDamage * getLevel());
            ((EntityDamageByEntityEvent) event).setDamage(damage);
        }
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

        setLevel(level);
        return true;
    }
}
