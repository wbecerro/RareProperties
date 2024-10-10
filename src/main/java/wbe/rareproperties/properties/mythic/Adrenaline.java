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

public class Adrenaline extends RareProperty {

    public Adrenaline(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "adrenaline", RareProperties.propertyConfig.adrenalineName);
        setDescription(RareProperties.propertyConfig.adrenalineDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        player.setHealth(player.getHealth() - RareProperties.propertyConfig.adrenalineHealth);
        PotionEffect potion = new PotionEffect(PotionEffectType.STRENGTH,
                RareProperties.propertyConfig.adrenalineStrengthDuration * 20 * getLevel(),
                RareProperties.propertyConfig.adrenalineStrength - 1);
        player.addPotionEffect(potion);
        potion = new PotionEffect(PotionEffectType.POISON,
                RareProperties.propertyConfig.adrenalinePoisonDuration * 20 * getLevel(),
                RareProperties.propertyConfig.adrenalinePoison - 1);
        player.addPotionEffect(potion);

        player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(
                RareProperties.propertyConfig.adrenalineSize * getLevel() + 1);
        player.playSound(player.getLocation(), Sound.valueOf(RareProperties.propertyConfig.adrenalineSoundOn),
                1F, 1F);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1);
                player.playSound(player.getLocation(),
                        Sound.valueOf(RareProperties.propertyConfig.adrenalineSoundOff),
                        1F, 0.7F);
            }
        }, RareProperties.propertyConfig.adrenalineStrengthDuration * 20L * getLevel());
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        PlayerInventory inventory = player.getInventory();
        level = checkHands(inventory, getExternalName());

        if(level < 0) {
            return false;
        }

        if(player.getHealth() <= RareProperties.propertyConfig.adrenalineHealth) {
            player.sendMessage(RareProperties.messages.notEnoughHealth);
            return false;
        }

        setLevel(level);
        return true;
    }
}
