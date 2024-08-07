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

public class Aegis extends RareProperty {

    public Aegis(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "aegis", "Égida");
        setDescription(getConfig().getStringList("Properties.Aegis.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        player.setHealth(player.getHealth() - getConfig().getInt("Properties.Aegis.healthCost"));
        PotionEffect potion = new PotionEffect(PotionEffectType.RESISTANCE, getConfig().getInt("Properties.Aegis.effectsDuration") * 20,
                getConfig().getInt("Properties.Aegis.resistanceLevel") - 1);
        player.addPotionEffect(potion);
        potion = new PotionEffect(PotionEffectType.ABSORPTION, getConfig().getInt("Properties.Aegis.effectsDuration") * 20,
                getConfig().getInt("Properties.Aegis.absortionLevel") * getLevel() - 1);
        player.addPotionEffect(potion);
        player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(getConfig().getDouble("Properties.Aegis.sizeMultiplier"));
        player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_REPAIR, 1F, 0.01F);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1);
                player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DAMAGE, 1F, 0.01F);
            }
        }, getConfig().getInt("Properties.Aegis.effectsDuration") * 20L);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if(!player.hasPermission("rareproperties.use.aegis")) {
            return false;
        }

        if(player.getHealth() <= getConfig().getInt("Properties.Aegis.healthCost")) {
            player.sendMessage(RareProperties.messages.notEnoughHealth);
        }

        PlayerInventory inventory = player.getInventory();
        level = checkHands(inventory, "Égida");

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
