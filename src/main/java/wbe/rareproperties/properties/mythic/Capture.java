package wbe.rareproperties.properties.mythic;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.Random;

public class Capture extends RareProperty {

    public Capture(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "capture", RareProperties.propertyConfig.captureName);
        setDescription(RareProperties.propertyConfig.captureDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        LivingEntity entity = ((EntityDeathEvent) event).getEntity();

        Random random = new Random();
        int prob = random.nextInt(RareProperties.propertyConfig.captureChance);
        if(getLevel() < prob) {
            return;
        }

        Material eggMaterial = Material.AIR;
        try {
            String conversion = entity.getType().toString() + "_SPAWN_EGG";

            eggMaterial = Material.valueOf(conversion);
        } catch(IllegalArgumentException ex) {
            return;
        }


        ItemStack egg = new ItemStack(eggMaterial);
        egg.setAmount(1);
        player.sendMessage(RareProperties.messages.captureSuccess);
        player.playSound(player.getLocation(), Sound.BLOCK_TRIAL_SPAWNER_ABOUT_TO_SPAWN_ITEM, 1F, 1F);
        ((EntityDeathEvent) event).getDrops().add(egg);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        level = checkProperty(item, getExternalName());

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
