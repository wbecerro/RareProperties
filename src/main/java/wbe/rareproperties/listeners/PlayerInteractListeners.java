package wbe.rareproperties.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.mythic.*;


public class PlayerInteractListeners implements Listener {

    private RareProperties plugin;

    public PlayerInteractListeners(RareProperties plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onUsingFirework(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                return;
            }
        }

        Player player = event.getPlayer();
        if(!player.isGliding()) {
            return;
        }

        ItemStack item = event.getItem();
        if(item == null || item.getType().equals(Material.AIR)) {
            return;
        }

        if(!item.getType().equals(Material.FIREWORK_ROCKET)) {
            return;
        }

        // Comprobación de Cohetería
        Rocketry rocketry = new Rocketry(plugin);
        boolean rocketryProperty = rocketry.checkUse(player, event);
        if(rocketryProperty) {
            rocketry.applyEffect(player, event);
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
        boolean repairProperty = repair.checkUse(player, event);
        if(repairProperty) {
            repair.applyEffect(player, event);
        }

        // Comprobación de Andanada
        Burst burst = new Burst(plugin);
        boolean burstProperty = burst.checkUse(player, event);
        if(burstProperty) {
            burst.applyEffect(player, event);
        }

        // Comprobación de Teletransporte
        Teleport teleport = new Teleport(plugin);
        boolean teleportProperty = teleport.checkUse(player, event);
        if(teleportProperty) {
            teleport.applyEffect(player, event);
        }

        // Comprobación de Égida
        Aegis aegis = new Aegis(plugin);
        boolean aegisProperty = aegis.checkUse(player, event);
        if(aegisProperty) {
            aegis.applyEffect(player, event);
        }

        // Comprobación de Presteza
        Promptness promptness = new Promptness(plugin);
        boolean promptnessProperty = promptness.checkUse(player, event);
        if(promptnessProperty) {
            promptness.applyEffect(player, event);
        }

        // Comprobación de Demolición
        Demolition demolition = new Demolition(plugin);
        boolean demolitionProperty = demolition.checkUse(player, event);
        if(demolitionProperty) {
            demolition.applyEffect(player, event);
        }

        // Comprobación de Propulsión
        Propulsion propulsion = new Propulsion(plugin);
        boolean propulsionProperty = propulsion.checkUse(player, event);
        if(propulsionProperty) {
            propulsion.applyEffect(player, event);
        }

        // Comprobación de Sanación
        Healing healing = new Healing(plugin);
        boolean healingProperty = healing.checkUse(player, event);
        if(healingProperty) {
            healing.applyEffect(player, event);
        }

        // Comprobación de Adrenalina
        Adrenaline adrenaline = new Adrenaline(plugin);
        boolean adrenalineProperty = adrenaline.checkUse(player, event);
        if(adrenalineProperty) {
            adrenaline.applyEffect(player, event);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void useTomeOnInteract(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                return;
            }
        }

        Player player = event.getPlayer();

        ItemStack tome = player.getInventory().getItemInMainHand();
        if(tome.getType() == Material.AIR) {
            return;
        } else if(tome.getItemMeta() == null) {
            return;
        }

        NamespacedKey key = new NamespacedKey(plugin, "IdentifierTome");
        if(!tome.getItemMeta().getPersistentDataContainer().has(key)) {
            return;
        }

        PlayerInventory inventory = player.getInventory();
        NamespacedKey itemKey = new NamespacedKey(plugin, "rarepropertiesunidentified");
        ItemStack inventoryItem = new ItemStack(Material.AIR);
        for(ItemStack item : inventory) {
            if(item == null) {
                continue;
            }

            if(item.getType() == Material.AIR) {
                continue;
            }

            if(item.getItemMeta() == null) {
                continue;
            }

            if(item.getItemMeta().getPersistentDataContainer().has(itemKey)) {
                inventoryItem = item;
                break;
            }
        }

        if(inventoryItem.getType().equals(Material.AIR)) {
            player.sendMessage(RareProperties.messages.noItemsToIdentify);
            event.setCancelled(true);
            return;
        }

        inventory.remove(inventoryItem);

        ItemStack item = RareProperties.itemManager.generateRandomItem(inventoryItem.getType());

        player.sendMessage(RareProperties.messages.itemIdentified);
        if(tome.getAmount() - 1 == 0) {
            inventory.setItemInMainHand(null);
        } else {
            tome.setAmount(tome.getAmount() - 1);
            inventory.setItemInMainHand(tome);
        }

        inventory.addItem(item);
        player.updateInventory();
        event.setCancelled(true);
    }
}
