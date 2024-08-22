package wbe.rareproperties.properties.legendary;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.List;

public class Swarm extends RareProperty {

    public Swarm(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "swarm", RareProperties.propertyConfig.swarmName);
        setDescription(RareProperties.propertyConfig.swarmDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        double damage = ((EntityDamageByEntityEvent) event).getDamage();
        int distance = RareProperties.propertyConfig.swarmDistance;
        List<Entity> entites = player.getNearbyEntities(distance, distance, distance);
        entites.removeIf(e -> !(e instanceof LivingEntity));

        int size = entites.size();
        int maxEntities = RareProperties.propertyConfig.swarmMaxEntities;
        int amount = Math.min(size, maxEntities);

        int maxPercent = RareProperties.propertyConfig.swarmPercent * getLevel();
        int damagePercentPerEntity = maxPercent / maxEntities;
        double currentPercent = (damagePercentPerEntity * amount) / 100.0;
        double modifier = 1 + currentPercent;
        damage = damage * modifier;
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

        setLevel(level);
        return true;
    }
}
