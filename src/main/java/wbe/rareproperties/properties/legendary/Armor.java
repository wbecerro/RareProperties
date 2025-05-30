package wbe.rareproperties.properties.legendary;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Armor extends RareProperty {

    private int armorAmount = 0;

    public Armor(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "armor", RareProperties.propertyConfig.armorName);
        setDescription(RareProperties.propertyConfig.armorDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        double damage = ((EntityDamageEvent) event).getDamage();
        damage = damage * (1 - RareProperties.propertyConfig.armorPercent * armorAmount);
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
                armorAmount += level;
            }
        }

        return -1;
    }
}
