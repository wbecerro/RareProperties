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
        super(plugin, new ArrayList<>(), "demolition", RareProperties.propertyConfig.demolitionName);
        setDescription(RareProperties.propertyConfig.demolitionDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        int radius = RareProperties.propertyConfig.demolitionRange + getLevel();
        double damage = RareProperties.propertyConfig.demolitionDamage + 2 * getLevel();

        if(!applyFoodCost(player, RareProperties.propertyConfig.demolitionCost)) {
            return;
        }

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
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = -1;

        PlayerInventory inventory = player.getInventory();
        level = checkHands(inventory, getExternalName());

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
