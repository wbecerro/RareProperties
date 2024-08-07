package wbe.rareproperties.properties.mythic;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.config.Messages;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Teleport extends RareProperty {

    public Teleport(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "teleport", "Teletransporte");
        setDescription(getConfig().getStringList("Properties.Teleport.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        int cost = getConfig().getInt("Properties.Teleport.baseCost");

        if(player.getFoodLevel() < cost) {
            return;
        }

        int distance = 2 + getConfig().getInt("Properties.Teleport.extraBlocksPerLevel") * getLevel();

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
        boolean tp = false;
        int level = 0;

        if(getConfig().getStringList("Messages.blacklistedWorlds").contains(player.getWorld().getName())) {
            return false;
        }

        if(!player.hasPermission("rareproperties.use.burst")) {
            return false;
        }

        PlayerInventory in = player.getInventory();
        if (!tp) {
            ItemStack hand = in.getItemInMainHand();
            level = checkProperty(hand, "Teletransporte");
            if(level > 0) {
                tp = true;
            } else {
                ItemStack offHand = in.getItemInOffHand();
                level = checkProperty(offHand, "Teletransporte");
                if(level > 0) {
                    tp = true;
                }
            }
        }

        if(!tp) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
