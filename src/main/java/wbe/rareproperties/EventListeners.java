package wbe.rareproperties;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import wbe.rareproperties.properties.*;

import java.text.Normalizer;

public class EventListeners implements Listener {
    private RareProperties plugin;

    private FileConfiguration config;

    public EventListeners(RareProperties plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerEquip(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Comprobación de Vuelo
        Fly fly = new Fly(plugin);
        boolean flyProperty = fly.checkUse(player);
        if(flyProperty) {
            fly.applyEffect(player, event);
            return;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteractRareItem(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }

        Player player = event.getPlayer();

        if(player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            return;
        } else if(player.getInventory().getItemInMainHand().getItemMeta() == null) {
            return;
        }

        // Comprobación de Reparación
        Repair repair = new Repair(plugin);
        boolean repairProperty = repair.checkUse(player);
        if(repairProperty) {
            repair.applyEffect(player, event);
            return;
        }

        // Comprobación de Andanada
        Burst burst = new Burst(plugin);
        boolean burstProperty = burst.checkUse(player);
        if(burstProperty) {
            burst.applyEffect(player, event);
            return;
        }

        // Comprobación de Teletransporte
        Teleport teleport = new Teleport(plugin);
        boolean teleportProperty = teleport.checkUse(player);
        if(teleportProperty) {
            teleport.applyEffect(player, event);
            return;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMobDeath(EntityDeathEvent event) {
        try {
            Player player = event.getEntity().getKiller();
            if(player == null) {
                return;
            }

            // Comprobación de Captura
            Capture capture = new Capture(plugin);
            boolean captureProperty = capture.checkUse(player);
            if(captureProperty) {
                capture.applyEffect(player, event);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
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
        NamespacedKey diabloKey = new NamespacedKey(plugin, "SindrisFavourDiablo");

        if(!meta.getPersistentDataContainer().has(propertyKey)) {
            return;
        }
        String property = meta.getPersistentDataContainer().get(propertyKey, PersistentDataType.STRING);
        String level = meta.getPersistentDataContainer().get(levelKey, PersistentDataType.STRING);
        boolean diablo = meta.getPersistentDataContainer().get(diabloKey, PersistentDataType.BOOLEAN);

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

        plugin.addProperty(newItem, property, level, "d", p, diablo);

        p.sendMessage(config.getString("Messages.propertyAddedDisc").replace("&", "§"));

        e.setCurrentItem(newItem);
        p.setItemOnCursor(null);
        e.setCancelled(true);
        p.updateInventory();
    }
}
