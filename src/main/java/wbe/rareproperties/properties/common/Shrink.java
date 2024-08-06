package wbe.rareproperties.properties.common;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
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

public class Shrink extends RareProperty {

    public Shrink(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "shrink", "Encogimiento");
        setDescription(getConfig().getStringList("Properties.Shrink.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        Random rand = new Random();
        int random = rand.nextInt(100);

        if (random > getLevel() * getConfig().getInt("Properties.Shrink.chancePerLevel")) {
            return;
        }

        LivingEntity damaged = (LivingEntity) ((EntityDamageByEntityEvent) event).getEntity();
        damaged.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(0.5);
        player.playSound(player.getLocation(), Sound.valueOf(getConfig().getString("Properties.Shrink.sound")), 1F, 1F);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
            @Override
            public void run() {
                damaged.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1);
            }
        }, getConfig().getInt("Properties.Shrink.durationPerLevel") * 20L);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if (!player.hasPermission("rareproperties.use.shrink")) {
            return false;
        }

        if (!utilities.hasProperty(player.getInventory().getItemInMainHand(), "Encogimiento")) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        level = checkProperty(item, "Encogimiento");

        if (level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
