package wbe.rareproperties.properties.mythic;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

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

        for(Location loc : getCircle(player.getLocation(),2,(2*((int)(Math.PI*2))))){
            Block block = loc.clone().subtract(0, 1, 0).getBlock();
            if(block.getType() == Material.AIR) {
                block = loc.clone().subtract(0, 2, 0).getBlock();
            }
            FallingBlock fb = loc.getWorld().spawnFallingBlock(loc, block.getBlockData());
            fb.setCancelDrop(true);
            fb.setVelocity(new Vector(0,0.25,0));
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
            @Override
            public void run() {
                for(Location loc : getCircle(player.getLocation(),3,(3*((int)(Math.PI*2))))){
                    Block block = loc.clone().subtract(0, 1, 0).getBlock();
                    if(block.getType() == Material.AIR) {
                        block = loc.clone().subtract(0, 2, 0).getBlock();
                    }
                    FallingBlock fb = loc.getWorld().spawnFallingBlock(loc, block.getBlockData());
                    fb.setCancelDrop(true);
                    fb.setVelocity(new Vector(0,0.35,0));
                }
            }
        }, 2L);

        List<Entity> entities = player.getNearbyEntities(radius, radius, radius);
        entities.removeIf(n -> (n instanceof Player));
        Vector playerVector = player.getLocation().toVector();
        for(Entity entity : entities) {
            if(entity instanceof LivingEntity) {
                ((LivingEntity) entity).damage(damage);
                Vector unitVector = entity.getLocation().toVector().subtract(playerVector).normalize();
                unitVector.setY(0.55);
                entity.setVelocity(unitVector.multiply(1));
            }
        }

        player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1F, 0.7F);
        player.setFoodLevel(player.getFoodLevel() - cost);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if(!player.hasPermission("rareproperties.use.demolition")) {
            return false;
        }

        if(player.getFoodLevel() < getConfig().getInt("Properties.Demolition.foodCost")) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        level = checkHands(inventory, "Demolición");

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }

    private ArrayList<Location> getCircle(Location center, double radius, int amount){
        World world = center.getWorld();
        double increment = ((2 * Math.PI) / amount);
        ArrayList<Location> locations = new ArrayList<>();
        for(int i = 0;i < amount; i++){
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }
}
