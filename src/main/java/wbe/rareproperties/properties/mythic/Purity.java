package wbe.rareproperties.properties.mythic;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectTypeCategory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Purity extends RareProperty {

    public Purity(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "purity", RareProperties.propertyConfig.purityName);
        setDescription(RareProperties.propertyConfig.purityDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        EntityPotionEffectEvent potionEffectEvent = (EntityPotionEffectEvent) event;
        PotionEffect potionEffect = potionEffectEvent.getNewEffect();
        if(!potionEffect.getType().getCategory().equals(PotionEffectTypeCategory.HARMFUL)) {
            return;
        }

        ((EntityPotionEffectEvent) event).setCancelled(true);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = -1;

        level = checkArmor(player.getInventory(), getExternalName());

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
