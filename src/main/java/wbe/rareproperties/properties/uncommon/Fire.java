package wbe.rareproperties.properties.uncommon;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Fire extends RareProperty {

    public Fire(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "fire", "Ígneo");
        setDescription(getConfig().getStringList("Properties.Fire.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        LivingEntity damaged = (LivingEntity) ((EntityDamageByEntityEvent) event).getEntity();
        damaged.setFireTicks(20 * (getLevel() * getConfig().getInt("Properties.Fire.timePerLevel")));
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if (!player.hasPermission("rareproperties.use.fire")) {
            return false;
        }

        if (!utilities.hasProperty(player.getInventory().getItemInMainHand(), "Ígneo")) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        level = checkProperty(item, "Ígneo");

        if (level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
