package wbe.rareproperties.properties;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import wbe.rareproperties.RareProperties;

import java.util.ArrayList;

public class Repair extends RareProperty {

    public Repair(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "repair", "Reparación");
        setDescription(getConfig().getStringList("Properties.Repair.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        PlayerInventory in = player.getInventory();

        int cost = getConfig().getInt("Properties.Repair.baseCost") - getLevel();
        ItemStack firstObject = in.getItem(0);
        Damageable meta = (Damageable) firstObject.getItemMeta();
        if (player.getFoodLevel() > cost && firstObject.getType() != Material.AIR && meta.getDamage() > 0) {
            int amount = Math.round(firstObject.getType().getMaxDurability() / 100 * getConfig().getInt("Properties.Repair.repairPercentage") * getLevel());
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
        boolean repair = false;
        int level = 0;

        if(!player.hasPermission("rareproperties.use.repair")) {
            return false;
        }

        PlayerInventory in = player.getInventory();
        if (!repair) {
            ItemStack hand = in.getItemInMainHand();
            level = checkProperty(hand, "Reparación");
            if (level > 0)
                repair = true;
        }

        if (!repair) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
