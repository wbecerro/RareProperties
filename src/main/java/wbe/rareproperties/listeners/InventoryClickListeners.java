package wbe.rareproperties.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.items.SindrisFavour;
import wbe.rareproperties.util.DecimalToRoman;
import wbe.rareproperties.util.RomanToDecimal;
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
        if(meta == null) {
            return;
        }

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

    @EventHandler(priority = EventPriority.NORMAL)
    public void addSocketOnClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(event.getAction() != InventoryAction.SWAP_WITH_CURSOR) {
            return;
        }

        NamespacedKey colorsKey = new NamespacedKey(plugin, "rarepropertiessocketslots");
        NamespacedKey socketKey = new NamespacedKey(plugin, "Socket");

        ItemStack handItem = event.getCursor();
        ItemStack inventoryItem = event.getCurrentItem();

        // Comprobaciones de los objetos.
        if(handItem == null || inventoryItem == null) {
            return;
        }

        if(handItem.getType().equals(Material.AIR) || inventoryItem.getType().equals(Material.AIR)) {
            return;
        }

        ItemMeta handItemMeta = handItem.getItemMeta();
        ItemMeta inventoryItemMeta = inventoryItem.getItemMeta();
        if(handItemMeta == null || inventoryItemMeta == null) {
            return;
        }

        if(!handItemMeta.getPersistentDataContainer().has(socketKey)) {
            return;
        }

        if(!inventoryItemMeta.getPersistentDataContainer().has(colorsKey)) {
            return;
        }

        ItemStack newItem = new ItemStack(inventoryItem.getType());
        newItem.setItemMeta(inventoryItemMeta);
        newItem.getItemMeta().setLore(inventoryItemMeta.getLore());
        boolean applied = utilities.applySocket(newItem,
                handItemMeta.getPersistentDataContainer().get(socketKey, PersistentDataType.STRING),
                inventoryItemMeta.getPersistentDataContainer().get(colorsKey, PersistentDataType.STRING));

        if(!applied) {
            player.sendMessage(RareProperties.messages.socketNoColor);
            return;
        }

        handItem.setAmount(handItem.getAmount() - 1);
        player.sendMessage(RareProperties.messages.socketApplied);
        event.setCurrentItem(newItem);
        player.setItemOnCursor(handItem);
        event.setCancelled(true);
        player.updateInventory();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPropertyUpgrade(InventoryClickEvent event) {
        if(!(event.getInventory() instanceof AnvilInventory)) {
            return;
        }

        if(event.getSlotType() != InventoryType.SlotType.RESULT) {
            return;
        }

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
        Player player = (Player) event.getWhoClicked();
        // Si el jugador tiene algo en la mano al coger la propiedad, ese objeto se suelta
        if(!player.getItemOnCursor().getType().equals(Material.AIR)) {
            player.getWorld().dropItem(player.getLocation(), player.getItemOnCursor());
        }

        event.setCancelled(true);
        event.getInventory().setItem(0, null);
        event.getInventory().setItem(1, null);

        player.setItemOnCursor(sindrisFavour);
    }
}