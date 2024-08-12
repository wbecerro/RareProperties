package wbe.rareproperties.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.util.Utilities;

public class InventoryClickListeners implements Listener {

    private RareProperties plugin;

    private Utilities utilities;

    public InventoryClickListeners(RareProperties plugin) {
        this.plugin = plugin;
        this.utilities = new Utilities(plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void addPropertyToItem(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getAction() != InventoryAction.SWAP_WITH_CURSOR) {
            return;
        }

        ItemStack playerItem = e.getCursor();
        ItemMeta meta = playerItem.getItemMeta();

        NamespacedKey propertyKey = new NamespacedKey(plugin, "SindrisFavourProperty");
        NamespacedKey levelKey = new NamespacedKey(plugin, "SindrisFavourLevel");

        if(!meta.getPersistentDataContainer().has(propertyKey)) {
            return;
        }
        String property = meta.getPersistentDataContainer().get(propertyKey, PersistentDataType.STRING);
        String level = meta.getPersistentDataContainer().get(levelKey, PersistentDataType.STRING);

        // Se añade la propiedad
        ItemStack inventoryItem = e.getCurrentItem();

        ItemMeta inventoryItemMeta = inventoryItem.getItemMeta();
        if(inventoryItem.getAmount() != 1) {
            return;
        }

        String name = "";
        if(!inventoryItemMeta.hasDisplayName()) {
            name = inventoryItemMeta.getItemName();
        } else {
            name = inventoryItemMeta.getDisplayName();
        }

        inventoryItemMeta.setDisplayName(name);
        inventoryItem.setItemMeta(inventoryItemMeta);

        ItemStack newItem = new ItemStack(inventoryItem.getType());
        newItem.setItemMeta(inventoryItemMeta);
        newItem.getItemMeta().setLore(inventoryItemMeta.getLore());

        boolean correct = utilities.addProperty(newItem, property, level, "d", p);
        if(!correct) {
            e.setCancelled(true);
            return;
        }

        p.sendMessage(RareProperties.messages.propertyAddedDisc);

        e.setCurrentItem(newItem);
        p.setItemOnCursor(null);
        e.setCancelled(true);
        p.updateInventory();
    }
}