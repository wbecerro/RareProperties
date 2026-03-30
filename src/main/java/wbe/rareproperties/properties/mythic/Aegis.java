package wbe.rareproperties.properties.mythic;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.AttributeModifiedPlayer;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Aegis extends RareProperty {

    private NamespacedKey attributeKey;

    public Aegis(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "aegis", RareProperties.propertyConfig.aegisName);
        setDescription(RareProperties.propertyConfig.aegisDescription);
        attributeKey = new NamespacedKey(plugin, "aegis");
    }

    @Override
    public void applyEffect(Player player, Event event) {
        if(!applyHealthCost(player, RareProperties.propertyConfig.aegisHealth)) {
            return;
        }

        PotionEffect potion = new PotionEffect(PotionEffectType.RESISTANCE, RareProperties.propertyConfig.aegisDuration * 20,
                RareProperties.propertyConfig.aegisResistance - 1);
        player.addPotionEffect(potion);
        potion = new PotionEffect(PotionEffectType.ABSORPTION, RareProperties.propertyConfig.aegisDuration * 20,
                RareProperties.propertyConfig.aegisAbsortion * getLevel() - 1);
        player.addPotionEffect(potion);

        AttributeModifier attributeModifier = new AttributeModifier(attributeKey, RareProperties.propertyConfig.aegisSize,
                AttributeModifier.Operation.ADD_SCALAR, EquipmentSlotGroup.ANY);

        AttributeModifier oldAttribute = utilities.searchModifier(player.getAttribute(Attribute.SCALE).getModifiers(), attributeKey);
        if(oldAttribute != null) {
            return;
        }

        player.getAttribute(Attribute.SCALE).addModifier(attributeModifier);
        player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_REPAIR, 1F, 0.01F);

        AttributeModifiedPlayer modifiedPlayer = new AttributeModifiedPlayer(player, Attribute.SCALE, attributeModifier);
        attributeModified.get(player).add(modifiedPlayer);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.getAttribute(Attribute.SCALE).removeModifier(attributeModifier);
                attributeModified.get(player).remove(modifiedPlayer);
                player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DAMAGE, 1F, 0.01F);
            }
        }, RareProperties.propertyConfig.aegisDuration * 20L);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = -1;

        PlayerInventory inventory = player.getInventory();
        level = checkHands(inventory, getExternalName());

        if(level < 0) {
            return false;
        }

        setLevel(level);
        return true;
    }
}
