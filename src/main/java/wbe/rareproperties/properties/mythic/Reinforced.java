package wbe.rareproperties.properties.mythic;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.Random;

public class Reinforced extends RareProperty {

    public Reinforced(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "reinforced", "Reforzado");
        setDescription(getConfig().getStringList("Properties.Reinforced.description"));
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
        int level = 0;

        if(!player.hasPermission("rareproperties.use.reinforced")) {
            return false;
        }

        ItemStack item = ((PlayerItemDamageEvent) event).getItem();
        level = checkProperty(item, "Reforzado");

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}