package wbe.rareproperties.properties.epic;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.Random;

public class Repulsion extends RareProperty {

    private int repulsionAmount = 0;

    public Repulsion(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "repulsion", RareProperties.propertyConfig.repulsionName);
        setDescription(RareProperties.propertyConfig.repulsionDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        int chance = RareProperties.propertyConfig.repulsionChance * repulsionAmount;
        Random random = new Random();
        if(random.nextInt(100) <= chance) {
            Vector projectileVelocity = ((ProjectileHitEvent) event).getEntity().getVelocity().clone();
            ((ProjectileHitEvent) event).getEntity().setVelocity(projectileVelocity.multiply(new Vector(-1, -1, -1)));
            ((ProjectileHitEvent) event).setCancelled(true);
        }
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
                repulsionAmount += level;
            }
        }

        return repulsionAmount > 0 ? repulsionAmount : -1;
    }
}
