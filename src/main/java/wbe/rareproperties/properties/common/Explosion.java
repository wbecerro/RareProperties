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
        super(plugin, new ArrayList<>(), "explosion", "Explosión");
        setDescription(getConfig().getStringList("Properties.Explosion.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        Random rand = new Random();
        int random = rand.nextInt(100);

        if (random > getLevel() * getConfig().getInt("Properties.Explosion.chancePerLevel")) {
            return;
        }

        LivingEntity damaged = (LivingEntity) ((EntityDamageByEntityEvent) event).getEntity();
        damaged.damage(getConfig().getInt("Properties.Explosion.damage"));
        damaged.getWorld().spawnEntity(damaged.getLocation(), EntityType.FIREWORK_ROCKET);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if (!player.hasPermission("rareproperties.use.explosion")) {
            return false;
        }

        if (!utilities.hasProperty(player.getInventory().getItemInMainHand(), "Explosión")) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        level = checkProperty(item, "Explosión");

        if (level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
