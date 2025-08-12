package wbe.rareproperties.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.items.SindrisFavour;
import wbe.rareproperties.util.DecimalToRoman;
import wbe.rareproperties.util.RomanToDecimal;
import wbe.rareproperties.util.Utilities;

public class PrepareAnvilListeners implements Listener {

    private RareProperties plugin;

    private Utilities utilities;

    public PrepareAnvilListeners(RareProperties plugin) {
        this.plugin = plugin;
        utilities = new Utilities(plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void showPropertyUpgrade(PrepareAnvilEvent event) {
        ItemStack first = event.getInventory().getItem(0);
        ItemStack second = event.getInventory().getItem(1);
        NamespacedKey propertyKey = new NamespacedKey(plugin, "SindrisFavourProperty");
        NamespacedKey levelKey = new NamespacedKey(plugin, "SindrisFavourLevel");

        if(!utilities.checkItem(first, propertyKey)) {
            return;
        }

        if(!utilities.checkItem(second, propertyKey)) {
            return;
        }

        ItemMeta firstMeta = first.getItemMeta();
        ItemMeta secondMeta = second.getItemMeta();
        if(!firstMeta.getPersistentDataContainer().get(propertyKey, PersistentDataType.STRING)
                .equalsIgnoreCase(secondMeta.getPersistentDataContainer().get(propertyKey, PersistentDataType.STRING))) {
            return;
        }

        if(!firstMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.STRING)
                .equalsIgnoreCase(secondMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.STRING))) {
            return;
        }

        int newLevel = RomanToDecimal.romanToDecimal(firstMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.STRING)) + 1;
        if(newLevel > RareProperties.config.maxUpgradeLevel) {
            return;
        }

        SindrisFavour sindrisFavour = new SindrisFavour();
        sindrisFavour.setProperty(utilities.getProperty(firstMeta.getPersistentDataContainer().get(propertyKey, PersistentDataType.STRING)),
                DecimalToRoman.intToRoman(newLevel),
                plugin);

        event.setResult(sindrisFavour);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void showOrichalcumUpgrade(PrepareAnvilEvent event) {
        ItemStack first = event.getInventory().getItem(0);
        ItemStack second = event.getInventory().getItem(1);
        NamespacedKey propertyKey = new NamespacedKey(plugin, "SindrisFavourProperty");
        NamespacedKey levelKey = new NamespacedKey(plugin, "SindrisFavourLevel");
        NamespacedKey key = new NamespacedKey(plugin, "OrichalcumShard");

        if(!utilities.checkItem(first, propertyKey)) {
            return;
        }

        if(!utilities.checkItem(second, key)) {
            return;
        }

        ItemMeta firstMeta = first.getItemMeta();
        int propertyLevel = RomanToDecimal.romanToDecimal(firstMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.STRING));
        if(propertyLevel > RareProperties.config.maxUpgradeLevel) {
            return;
        }

        int newLevel = propertyLevel + 1;

        SindrisFavour sindrisFavour = new SindrisFavour();
        sindrisFavour.setProperty(utilities.getProperty(firstMeta.getPersistentDataContainer().get(propertyKey, PersistentDataType.STRING)),
                DecimalToRoman.intToRoman(newLevel),
                plugin);

        event.setResult(sindrisFavour);
    }
}
