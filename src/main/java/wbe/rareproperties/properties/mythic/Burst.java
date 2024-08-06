package wbe.rareproperties.properties.mythic;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.List;

public class Burst extends RareProperty {

    public Burst(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "burst", "Andanada");
        setDescription(getConfig().getStringList("Properties.Burst.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        int cost = getConfig().getInt("Properties.Burst.baseCost") - getLevel();

        if(player.getFoodLevel() < cost) {
            return;
        }

        List<Entity> nearbyEntities = player.getNearbyEntities(8, 8, 8);
        Vector vPlayer = player.getLocation().toVector();

        player.playSound(player.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1F, 1F);
        for(Entity ent : nearbyEntities) {
            Vector unitVector = ent.getLocation().toVector().subtract(vPlayer).normalize();
            unitVector.setY(0.55 / getLevel());
            ent.setVelocity(unitVector.multiply(getLevel() * getConfig().getInt("Properties.Burst.baseVelocity")));
        }

        player.setFoodLevel(player.getFoodLevel() - cost);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if(getConfig().getStringList("Messages.blacklistedWorlds").contains(player.getWorld().getName())) {
            return false;
        }

        if(!player.hasPermission("rareproperties.use.burst")) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        level = checkHands(inventory, "Andanada");

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
