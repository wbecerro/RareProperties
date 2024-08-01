package wbe.rareproperties.properties;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import wbe.rareproperties.RareProperties;

import java.util.ArrayList;

public class Promptness extends RareProperty {

    public Promptness(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "promptness", "Presteza");
        setDescription(getConfig().getStringList("Properties.Promptness.description"));
    }

    @Override
    public void applyEffect(Player player, Event event) {
        player.setHealth(player.getHealth() - getConfig().getInt("Properties.Promptness.healthCost"));
        PotionEffect potion = new PotionEffect(PotionEffectType.SPEED, getConfig().getInt("Properties.Promptness.effectsDuration") * 20,
                getConfig().getInt("Properties.Promptness.speedLevel") * getLevel() - 1);
        player.addPotionEffect(potion);
        player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(getConfig().getDouble("Properties.Promptness.sizeMultiplier"));
        player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_IDLE_AIR, 1F, 0.01F);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1);
                player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_IDLE_GROUND, 1F, 0.01F);
            }
        }, getConfig().getInt("Properties.Promptness.effectsDuration") * 20L);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        boolean promptness = false;
        int level = 0;

        if(!player.hasPermission("rareproperties.use.promptness")) {
            return false;
        }

        if(player.getHealth() <= getConfig().getInt("Properties.Promptness.healthCost")) {
            player.sendMessage(getConfig().getString("Messages.notEnoughHealth").replace("&", "§"));
        }

        PlayerInventory in = player.getInventory();
        if(!promptness) {
            ItemStack hand = in.getItemInMainHand();
            level = checkProperty(hand, "Presteza");
            if (level > 0)
                promptness = true;
        }

        if(!promptness) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
