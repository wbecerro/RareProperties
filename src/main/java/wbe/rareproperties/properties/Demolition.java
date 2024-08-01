package wbe.rareproperties.properties;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;

import java.util.ArrayList;
import java.util.List;

public class Demolition extends RareProperty {

    public Demolition(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "demolition", "Demolición");
        setDescription(getConfig().getStringList("Properties.Demolition.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        int cost = getConfig().getInt("Properties.Demolition.foodCost");

        int radius = getConfig().getInt("Properties.Demolition.baseRange") + getLevel();
        int damage = getConfig().getInt("Properties.Demolition.baseDamage") + 2 * getLevel();
        List<Entity> entities = player.getNearbyEntities(radius, radius, radius);
        entities.removeIf(n -> (n instanceof Player));
        for(Entity entity : entities) {
            if(entity instanceof LivingEntity) {
                ((LivingEntity) entity).damage(damage);
            }
        }

        player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1F, 0.7F);
        player.setFoodLevel(player.getFoodLevel() - cost);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        boolean demolition = false;
        int level = 0;

        if(!player.hasPermission("rareproperties.use.demolition")) {
            return false;
        }

        if(player.getFoodLevel() < getConfig().getInt("Properties.Demolition.foodCost")) {
            return false;
        }

        PlayerInventory in = player.getInventory();
        if(!demolition) {
            ItemStack hand = in.getItemInMainHand();
            level = checkProperty(hand, "Demolición");
            if (level > 0)
                demolition = true;
        }

        if(!demolition) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
