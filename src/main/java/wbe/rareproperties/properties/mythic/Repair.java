package wbe.rareproperties.properties.mythic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Repair extends RareProperty {

    public Repair(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "repair", RareProperties.propertyConfig.repairName);
        setDescription(RareProperties.propertyConfig.repairDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        PlayerInventory in = player.getInventory();

        int cost = RareProperties.propertyConfig.repairCost - getLevel();
        ItemStack firstObject = in.getItem(0);
        Damageable meta = (Damageable) firstObject.getItemMeta();
        if (player.getFoodLevel() > cost && firstObject.getType() != Material.AIR && meta.getDamage() > 0) {
            int amount = Math.round(firstObject.getType().getMaxDurability() / 100 * RareProperties.propertyConfig.repairPercent * getLevel());
            int durability = (meta.getDamage() - amount);

            if (durability < 0) {
                durability = 0;
            }

            player.setFoodLevel(player.getFoodLevel() - cost);
            meta.setDamage(durability);
            firstObject.setItemMeta(meta);

            player.updateInventory();
        }
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        level = checkHands(player.getInventory(), getExternalName());

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
