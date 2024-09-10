package wbe.rareproperties.properties.epic;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.Random;

public class Critic extends RareProperty {

    public Critic(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "critic", RareProperties.propertyConfig.criticName);
        setDescription(RareProperties.propertyConfig.criticDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        double damage = ((EntityDamageByEntityEvent) event).getDamage();

        Random rand = new Random();
        int random = rand.nextInt(100);

        if(random > getLevel() * RareProperties.propertyConfig.criticChance) {
            return;
        }

        damage = damage * (1 + getLevel() * RareProperties.propertyConfig.criticDamage);
        ((EntityDamageByEntityEvent) event).setDamage(damage);
        player.playSound(player.getLocation(), Sound.valueOf(RareProperties.propertyConfig.criticSound), 0.5F, 1.0F);
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
