package wbe.rareproperties.properties;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import wbe.rareproperties.RareProperties;

import java.util.Random;

public class Reinforced extends RareProperty {

    public Reinforced(RareProperties plugin) {
        super(plugin);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        Random random = new Random();
        int prob = random.nextInt(100);
        if (getLevel() * getConfig().getInt("Properties.Reinforced.baseProbability") > prob) {
            ((PlayerItemDamageEvent) event).setCancelled(true);
        }
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        boolean reinforced = false;
        int level = 0;

        if(!player.hasPermission("rareproperties.use.reinforced")) {
            return false;
        }

        ItemStack item = ((PlayerItemDamageEvent) event).getItem();
        if(!reinforced) {
            level = checkProperty(item, "Reforzado");
            if(level > 0) {
                reinforced = true;
            }
        }

        if(!reinforced) {
            return false;
        }

        setLevel(level);
        return true;
    }
}