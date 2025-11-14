package wbe.rareproperties.properties.mythic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.Random;

public class Rocketry extends RareProperty {

    public Rocketry(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "rocketry", RareProperties.propertyConfig.rocketryName);
        setDescription(RareProperties.propertyConfig.rocketryDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        Random random = new Random();
        int randomChance = random.nextInt(100) + 1;
        if(RareProperties.propertyConfig.rocketryChance * getLevel() > randomChance) {
            ItemStack firework = ((PlayerInteractEvent) event).getItem();
            firework.setAmount(firework.getAmount() + 1);
        }
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getChestplate();
        if(!item.getType().equals(Material.ELYTRA)) {
            return false;
        }

        level = checkProperty(item, getExternalName());

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
