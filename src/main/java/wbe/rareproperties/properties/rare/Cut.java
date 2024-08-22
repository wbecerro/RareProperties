package wbe.rareproperties.properties.rare;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Cut extends RareProperty {

    public Cut(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "cut", RareProperties.propertyConfig.cutName);
        setDescription(RareProperties.propertyConfig.cutDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        double damage = ((EntityDamageByEntityEvent) event).getDamage();
        damage = damage + RareProperties.propertyConfig.cutDamage * getLevel();
        ((EntityDamageByEntityEvent) event).setDamage(damage);
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
