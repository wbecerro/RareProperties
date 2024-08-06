package wbe.rareproperties.properties.common;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
import java.util.Random;

public class Enlarge extends RareProperty {

    public Enlarge(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "enlarge", "Agrandamiento");
        setDescription(getConfig().getStringList("Properties.Enlarge.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        Random rand = new Random();
        int random = rand.nextInt(100);

        if (random > getLevel() * getConfig().getInt("Properties.Enlarge.chancePerLevel")) {
            return;
        }

        LivingEntity damaged = (LivingEntity) ((EntityDamageByEntityEvent) event).getEntity();
        damaged.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(2);
        player.playSound(player.getLocation(), Sound.valueOf(getConfig().getString("Properties.Enlarge.sound")), 1F, 1F);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
            @Override
            public void run() {
                damaged.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1);
            }
        }, getConfig().getInt("Properties.Enlarge.durationPerLevel") * 20L);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if (!player.hasPermission("rareproperties.use.enlarge")) {
            return false;
        }

        if (!utilities.hasProperty(player.getInventory().getItemInMainHand(), "Agrandamiento")) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        level = checkProperty(item, "Agrandamiento");

        if (level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
