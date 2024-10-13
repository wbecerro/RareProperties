package wbe.rareproperties.properties.rare;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.Random;

public class Electro extends RareProperty {

    public Electro(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "electro", RareProperties.propertyConfig.electroName);
        setDescription(RareProperties.propertyConfig.electroDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        double damage = ((EntityDamageByEntityEvent) event).getDamage();

        Random rand = new Random();
        int random = rand.nextInt(100);

        if(random > getLevel() * RareProperties.propertyConfig.electroChance) {
            return;
        }

        damage = damage + getLevel() * RareProperties.propertyConfig.electroDamage;
        ((EntityDamageByEntityEvent) event).setDamage(damage);
        Entity entity = ((EntityDamageByEntityEvent) event).getEntity();
        entity.getWorld().strikeLightningEffect(entity.getLocation());
        player.playSound(player.getLocation(), Sound.valueOf(RareProperties.propertyConfig.electroSound), 1.0F, 1.0F);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if(!utilities.hasProperty(player.getInventory().getItemInMainHand(), getExternalName())) {
            return false;
        }

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
