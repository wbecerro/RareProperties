package wbe.rareproperties.properties.rare;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.Random;

public class Poison extends RareProperty {

    public Poison(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "poison", RareProperties.propertyConfig.poisonName);
        setDescription(RareProperties.propertyConfig.poisonDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        Random rand = new Random();
        int random = rand.nextInt(100);

        if (random > getLevel() * RareProperties.propertyConfig.poisonChance) {
            return;
        }

        LivingEntity damaged = (LivingEntity) ((EntityDamageByEntityEvent) event).getEntity();
        PotionEffect potion = new PotionEffect(PotionEffectType.POISON,
                RareProperties.propertyConfig.poisonDuration * 20,
                RareProperties.propertyConfig.poisonModifier - 1);
        damaged.addPotionEffect(potion);
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
