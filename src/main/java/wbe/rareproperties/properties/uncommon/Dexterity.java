package wbe.rareproperties.properties.uncommon;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.AttributeModifiedPlayer;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.Random;

public class Dexterity extends RareProperty {

    private NamespacedKey attributeKey;

    public Dexterity(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "dexterity", RareProperties.propertyConfig.dexterityName);
        setDescription(RareProperties.propertyConfig.dexterityDescription);
        attributeKey = new NamespacedKey(plugin, "dexterity");
    }

    @Override
    public void applyEffect(Player player, Event event) {
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        if(randomNumber > RareProperties.propertyConfig.dexterityChance * getLevel()) {
            return;
        }

        double speed = RareProperties.propertyConfig.dexteritySpeed * getLevel();
        AttributeModifier attributeModifier = new AttributeModifier(attributeKey, speed,
                AttributeModifier.Operation.ADD_SCALAR, EquipmentSlotGroup.ANY);

        AttributeModifier oldAttribute = utilities.searchModifier(player.getAttribute(Attribute.MOVEMENT_SPEED).getModifiers(), attributeKey);
        if(oldAttribute != null) {
            return;
        }

        player.getAttribute(Attribute.MOVEMENT_SPEED).addModifier(attributeModifier);
        AttributeModifiedPlayer modifiedPlayer = new AttributeModifiedPlayer(player, Attribute.MOVEMENT_SPEED, attributeModifier);
        attributeModified.get(player).add(modifiedPlayer);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.getAttribute(Attribute.MOVEMENT_SPEED).removeModifier(attributeModifier);
                attributeModified.get(player).remove(modifiedPlayer);
            }
        }, RareProperties.propertyConfig.dexterityDuration * 20L * getLevel());
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
