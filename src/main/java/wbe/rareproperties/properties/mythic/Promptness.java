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

public class Promptness extends RareProperty {

    private NamespacedKey attributeKey;

    public Promptness(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "promptness", RareProperties.propertyConfig.promptnessName);
        setDescription(RareProperties.propertyConfig.promptnessDescription);
        attributeKey = new NamespacedKey(plugin, "promptness");
    }

    @Override
    public void applyEffect(Player player, Event event) {
        if(!applyHealthCost(player, RareProperties.propertyConfig.promptnessHealth)) {
             return;
        }

        PotionEffect potion = new PotionEffect(PotionEffectType.SPEED, RareProperties.propertyConfig.promptnessDuration * 20,
                RareProperties.propertyConfig.promptnessSpeed * getLevel() - 1);
        player.addPotionEffect(potion);

        AttributeModifier attributeModifier = new AttributeModifier(attributeKey, RareProperties.propertyConfig.promptnessSize,
                AttributeModifier.Operation.ADD_SCALAR, EquipmentSlotGroup.ANY);

        AttributeModifier oldAttribute = utilities.searchModifier(player.getAttribute(Attribute.SCALE).getModifiers(), attributeKey);
        if(oldAttribute != null) {
            return;
        }

        player.getAttribute(Attribute.SCALE).addModifier(attributeModifier);
        player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_IDLE_AIR, 1F, 0.01F);

        AttributeModifiedPlayer modifiedPlayer = new AttributeModifiedPlayer(player, Attribute.SCALE, attributeModifier);
        attributeModified.get(player).add(modifiedPlayer);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.getAttribute(Attribute.SCALE).removeModifier(attributeModifier);
                attributeModified.get(player).remove(modifiedPlayer);
                player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_IDLE_GROUND, 1F, 0.01F);
            }
        }, RareProperties.propertyConfig.promptnessDuration * 20L);
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
