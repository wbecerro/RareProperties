package wbe.rareproperties.properties.epic;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Bestiality extends RareProperty {

    private int bestialityLevel = 0;

    public Bestiality(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "bestiality", RareProperties.propertyConfig.bestialityName);
        setDescription(RareProperties.propertyConfig.bestialityDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        double damage = ((EntityDamageEvent) event).getDamage();
        damage = damage * (1 + RareProperties.propertyConfig.bestialityDamage * bestialityLevel);
        ((EntityDamageEvent) event).setDamage(damage);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        PlayerInventory inventory = player.getInventory();
        level = checkArmor(inventory, getExternalName());

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }

    @Override
    public int checkArmor(PlayerInventory inventory, String property) {
        ItemStack[] armor = inventory.getArmorContents();
        int level = 0;
        for(ItemStack item : armor) {
            if(item == null) {
                continue;
            }

            level = checkProperty(item, property);
            if(level > 0) {
                bestialityLevel += level;
            }
        }

        return bestialityLevel > 0 ? bestialityLevel : -1;
    }
}
