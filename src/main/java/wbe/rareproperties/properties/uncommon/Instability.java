package wbe.rareproperties.properties.uncommon;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;
import wbe.rareproperties.util.Utilities;

import java.util.ArrayList;

public class Instability extends RareProperty {

    private Utilities utilities;

    public Instability(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "instability", RareProperties.propertyConfig.instabilityName);
        setDescription(RareProperties.propertyConfig.instabilityDescription);
        utilities = new Utilities(plugin);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        Arrow projectile = (Arrow) ((EntityShootBowEvent) event).getProjectile();
        int duration = RareProperties.propertyConfig.instabilityDuration * getLevel() * 20;
        int modifier = RareProperties.propertyConfig.instabilityModifier - 1;
        PotionEffect potionEffect = new PotionEffect(utilities.getRandomPotionEffect(), duration, modifier);
        projectile.addCustomEffect(potionEffect, false);
        ((EntityShootBowEvent) event).setProjectile(projectile);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 0;

        if(!utilities.hasProperty(player.getInventory().getItemInMainHand(), getExternalName())) {
            return false;
        }

        ItemStack item = ((EntityShootBowEvent) event).getBow();
        level = checkProperty(item, getExternalName());

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
