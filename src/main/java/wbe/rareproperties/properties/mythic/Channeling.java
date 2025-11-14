package wbe.rareproperties.properties.mythic;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.HashMap;

public class Channeling extends RareProperty {

    public static HashMap<Player, Integer> playersChanneling = new HashMap<>();

    public Channeling(RareProperties plugin) {
        super(plugin, new ArrayList<>(), "channeling", RareProperties.propertyConfig.channelingName);
        setDescription(RareProperties.propertyConfig.channelingDescription);
    }

    @Override
    public void applyEffect(Player player, Event event) {
        if(!playersChanneling.containsKey(player)) {
            playersChanneling.put(player, getLevel());
            changeMaxAbsorption(10000, player);
            player.setAbsorptionAmount(RareProperties.propertyConfig.channelingHalfHearts * getLevel());
        }
    }

    @Override
    public boolean checkUse(Player player, Event event) {
        int level = -1;

        PlayerInventory inventory = player.getInventory();
        level = checkArmorAccumulative(inventory, getExternalName());

        if(level < 0) {
            Integer value = Channeling.playersChanneling.remove(player);
            if(value != null) {
                player.setAbsorptionAmount(0);
            }
            return false;
        }

        setLevel(level);
        return true;
    }

    private void changeMaxAbsorption(double max, Player player) {
        if(player.getAttribute(Attribute.MAX_ABSORPTION).getValue() > 0) {
            return;
        }

        NamespacedKey key = new NamespacedKey(getPlugin(), "RarePropertiesMaxAbsorption");
        AttributeModifier attributeModifier =  new AttributeModifier(key, max, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY);
        player.getAttribute(Attribute.MAX_ABSORPTION).addModifier(attributeModifier);
    }
}
