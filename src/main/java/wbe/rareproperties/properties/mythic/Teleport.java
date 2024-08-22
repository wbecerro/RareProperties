package wbe.rareproperties.properties.mythic;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Teleport extends RareProperty {

    public Teleport(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "teleport", RareProperties.propertyConfig.teleportName);
        setDescription(RareProperties.propertyConfig.teleportDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        int cost = RareProperties.propertyConfig.teleportCost;

        if(player.getFoodLevel() < cost) {
            return;
        }

        int distance = 2 + RareProperties.propertyConfig.teleportBlocks * getLevel();

        Location playerLocation = player.getLocation();
        Vector direction = playerLocation.getDirection();

        direction.normalize();
        direction.multiply(distance);
        playerLocation.add(direction);

        if(playerLocation.getBlock().isEmpty() || playerLocation.getBlock().isPassable()) {
            if(playerLocation.add(0, 1, 0).getBlock().isEmpty() || playerLocation.add(0, 1, 0).getBlock().isPassable()) {
                player.teleport(playerLocation);
                player.setFoodLevel(player.getFoodLevel() - cost);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
                return;
            }
        }
        player.sendMessage(RareProperties.messages.cannotTeleport);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if(RareProperties.config.blacklistedWorlds.contains(player.getWorld().getName())) {
            return false;
        }

        level = checkHands(player.getInventory(), getExternalName());

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
