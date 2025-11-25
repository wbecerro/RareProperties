package wbe.rareproperties.properties.legendary;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Execution extends RareProperty {

    public Execution(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "execution", RareProperties.propertyConfig.executionName);
        setDescription(RareProperties.propertyConfig.executionDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        double damage = ((EntityDamageByEntityEvent) event).getDamage();
        LivingEntity entity = (LivingEntity) ((EntityDamageByEntityEvent) event).getEntity();
        int healthPercent = (int) ((entity.getHealth() / entity.getAttribute(Attribute.MAX_HEALTH).getValue()) * 100);
        int missingPercent = 100 - healthPercent;
        double percent = 1 + ((missingPercent * (RareProperties.propertyConfig.executionExtraDamagePerPercentPerLevel * getLevel())) / 100);

        damage = damage * percent;
        ((EntityDamageByEntityEvent) event).setDamage(damage);
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
