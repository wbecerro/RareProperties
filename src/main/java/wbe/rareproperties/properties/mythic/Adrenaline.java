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
import wbe.rareproperties.config.Messages;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Adrenaline extends RareProperty {

    public Adrenaline(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "adrenaline", "Adrenalina");
        setDescription(getConfig().getStringList("Properties.Adrenaline.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        player.setHealth(player.getHealth() - getConfig().getInt("Properties.Adrenaline.healthCost"));
        PotionEffect potion = new PotionEffect(PotionEffectType.STRENGTH,
                getConfig().getInt("Properties.Adrenaline.durationPerLevelStrength") * 20 * getLevel(),
                getConfig().getInt("Properties.Adrenaline.strengthLevel") - 1);
        player.addPotionEffect(potion);
        potion = new PotionEffect(PotionEffectType.POISON,
                getConfig().getInt("Properties.Adrenaline.durationPerLevelPoison") * 20 * getLevel(),
                getConfig().getInt("Properties.Adrenaline.poisonLevel") - 1);
        player.addPotionEffect(potion);

        player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(
                getConfig().getDouble("Properties.Adrenaline.sizePerLevel") * getLevel() + 1);
        player.playSound(player.getLocation(), Sound.valueOf(getConfig().getString("Properties.Adrenaline.sound")),
                1F, 1F);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1);
                player.playSound(player.getLocation(),
                        Sound.valueOf(getConfig().getString("Properties.Adrenaline.soundOff")),
                        1F, 0.7F);
            }
        }, getConfig().getInt("Properties.Adrenaline.durationPerLevelStrength") * 20L * getLevel());
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if(!player.hasPermission("rareproperties.use.adrenaline")) {
            return false;
        }

        if(player.getHealth() <= getConfig().getInt("Properties.Adrenaline.healthCost")) {
            player.sendMessage(RareProperties.messages.notEnoughHealth);
        }

        PlayerInventory inventory = player.getInventory();
        level = checkHands(inventory, "Adrenalina");

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
