package wbe.rareproperties.properties;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;
import wbe.rareproperties.RareProperties;

public class Propulsion extends RareProperty {

    public Propulsion(RareProperties plugin) {
        super(plugin);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        int cost = getConfig().getInt("Properties.Propulsion.foodCost");

        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1F, 1F);
        player.setVelocity(new Vector(0, getConfig().getInt("Properties.Propulsion.baseImpulse") * getLevel(), 0));
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        boolean propulsion = false;
        int level = 0;

        if(!player.hasPermission("rareproperties.use.propulsion")) {
            return false;
        }

        if(player.getFoodLevel() < getConfig().getInt("Properties.Propulsion.foodCost")) {
            return false;
        }

        PlayerInventory in = player.getInventory();
        if(!propulsion) {
            ItemStack hand = in.getItemInMainHand();
            level = checkProperty(hand, "Propulsión");
            if (level > 0)
                propulsion = true;
        }

        if(!propulsion) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
