package wbe.rareproperties.properties.common;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.Random;

public class Explosion extends RareProperty {

    public Explosion(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "explosion", RareProperties.propertyConfig.explosionName);
        setDescription(RareProperties.propertyConfig.explosionDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        Random rand = new Random();
        int random = rand.nextInt(100);

        if (random > getLevel() * RareProperties.propertyConfig.explosionChance) {
            return;
        }

        LivingEntity damaged = (LivingEntity) ((EntityDamageByEntityEvent) event).getEntity();
        damaged.damage(RareProperties.propertyConfig.explosionDamage);
        damaged.getWorld().spawnEntity(damaged.getLocation(), EntityType.FIREWORK_ROCKET);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if(!utilities.hasProperty(player.getInventory().getItemInMainHand(), getExternalName())) {
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
