package wbe.rareproperties.properties.mythic;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Aegis extends RareProperty {

    public Aegis(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "aegis", RareProperties.propertyConfig.aegisName);
        setDescription(RareProperties.propertyConfig.aegisDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        player.setHealth(player.getHealth() - RareProperties.propertyConfig.aegisHealth);
        PotionEffect potion = new PotionEffect(PotionEffectType.RESISTANCE, RareProperties.propertyConfig.aegisDuration * 20,
                RareProperties.propertyConfig.aegisResistance - 1);
        player.addPotionEffect(potion);
        potion = new PotionEffect(PotionEffectType.ABSORPTION, RareProperties.propertyConfig.aegisDuration * 20,
                RareProperties.propertyConfig.aegisAbsortion * getLevel() - 1);
        player.addPotionEffect(potion);
        player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(RareProperties.propertyConfig.aegisSize);
        player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_REPAIR, 1F, 0.01F);
        RareProperty.scaleModified.put(player, true);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1);
                player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DAMAGE, 1F, 0.01F);
            }
        }, RareProperties.propertyConfig.aegisDuration * 20L);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = -1;

        PlayerInventory inventory = player.getInventory();
        level = checkHands(inventory, getExternalName());

        if(level < 0) {
            return false;
        }

        if(player.getHealth() <= RareProperties.propertyConfig.aegisHealth) {
            player.sendMessage(RareProperties.messages.notEnoughHealth);
            return false;
        }

        setLevel(level);
        return true;
    }
}
