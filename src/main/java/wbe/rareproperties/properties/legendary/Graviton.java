package wbe.rareproperties.properties.legendary;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.AttributeModifiedPlayer;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;

public class Graviton extends RareProperty {

    private NamespacedKey attributeKey;

    public Graviton(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "graviton", RareProperties.propertyConfig.gravitonName);
        setDescription(RareProperties.propertyConfig.gravitonDescription);
        attributeKey = new NamespacedKey(plugin, "graviton");
    }

    @Override
    public void applyEffect(Player player, Event event) {
        double gravity = RareProperties.propertyConfig.gravityPerLevel * getLevel();
        AttributeModifier attributeModifier = new AttributeModifier(attributeKey, gravity,
                AttributeModifier.Operation.ADD_SCALAR, EquipmentSlotGroup.ANY);

        AttributeModifier oldAttribute = utilities.searchModifier(player.getAttribute(Attribute.GRAVITY).getModifiers(), attributeKey);
        if(oldAttribute != null) {
            return;
        }

        player.getAttribute(Attribute.GRAVITY).addModifier(attributeModifier);
        AttributeModifiedPlayer modifiedPlayer = new AttributeModifiedPlayer(player, Attribute.GRAVITY, attributeModifier);
        attributeModified.get(player).add(modifiedPlayer);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.getAttribute(Attribute.GRAVITY).removeModifier(attributeModifier);
                attributeModified.get(player).remove(modifiedPlayer);
            }
        }, RareProperties.propertyConfig.gravitonDuration * 20L);
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = 1;

        PlayerInventory inventory = player.getInventory();
        level = checkHands(inventory, getExternalName());

        if(level < 0) {
            return false;
        }

        if(!inventory.getItemInMainHand().getType().equals(Material.MACE)) {
            if(!inventory.getItemInOffHand().getType().equals(Material.MACE)) {
                return false;
            }
        }

        setLevel(level);
        return true;
    }
}
