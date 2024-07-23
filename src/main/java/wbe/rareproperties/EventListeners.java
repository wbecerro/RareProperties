package wbe.rareproperties;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.text.Normalizer;
import java.util.*;

public class EventListeners implements Listener {
    private RareProperties plugin;

    private FileConfiguration config;

    static ItemStack objeto = null;

    public static HashMap<Player, Integer> cobrarComida = new HashMap<Player, Integer>();

    Runnable bkr = null;

    public EventListeners(RareProperties plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerEquip(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
            boolean fly = false;
            int level = 0;
            // Fly
            if (player.hasPermission("rareproperties.use.fly")) {
                PlayerInventory in = player.getInventory();
                ItemStack[] arrayOfItemStack = in.getArmorContents();
                for(ItemStack is : arrayOfItemStack) {
                    if(is == null) {
                        continue;
                    }
                    level = comprobacionObjeto(is, "Vuelo");
                    if(level > 0) {
                        fly = true;
                        break;
                    }
                }

                // Si no hay fly en la armadura se comprueba la mano primaria y luego la secundaria.
                if(!fly) {
                    ItemStack hand = in.getItemInMainHand();
                    level = comprobacionObjeto(hand, "Vuelo");
                    if(level > 0) {
                        fly = true;
                    } else {
                        ItemStack offHand = in.getItemInOffHand();
                        level = comprobacionObjeto(offHand, "Vuelo");
                        if(level > 0) {
                            fly = true;
                        }
                    }
                }

                // Si no se encuentra Vuelo en armor o item en la mano sale.
                if(!fly) {
                    cobrarComida.remove(player);
                    player.setAllowFlight(false);
                    return;
                }

                if (player.getFoodLevel() <= 0) {
                    player.setAllowFlight(false);
                } else {
                    player.setAllowFlight(true);
                    player.setFlySpeed(0.1F);
                }

                cobrarComida.put(player, level);
            }
        } else {
            cobrarComida.remove(player);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteractRareItem(PlayerInteractEvent event) {
        boolean repair = false;
        boolean burst = false;
        boolean tp = false;
        int level = 0;

        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }

        Player player = event.getPlayer();

        if(player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            return;
        } else if(player.getInventory().getItemInMainHand().getItemMeta() == null) {
            return;
        }

        if(plugin.hasProperty(player.getInventory().getItemInMainHand(), "Reparación")) {
            if (!player.hasPermission("rareproperties.use.repair")) {
                return;
            } else {
                PlayerInventory in = player.getInventory();
                if (!repair) {
                    ItemStack hand = in.getItemInMainHand();
                    level = comprobacionObjeto(hand, "Reparación");
                    if (level > 0)
                        repair = true;
                }

                if (!repair) {
                    return;
                }

                int coste = 10 - level;
                ItemStack primerObjeto = in.getItem(0);
                Damageable meta = (Damageable) primerObjeto.getItemMeta();
                if (player.getFoodLevel() > coste && primerObjeto != null && meta.getDamage() > 0) {
                    int cantidad = Math.round(primerObjeto.getType().getMaxDurability() / 100 * 10 * level);
                    int durabilidad = (meta.getDamage() - cantidad);

                    if (durabilidad < 0) {
                        durabilidad = 0;
                    }

                    player.setFoodLevel(player.getFoodLevel() - coste);
                    meta.setDamage(durabilidad);
                    primerObjeto.setItemMeta(meta);

                    //in.setItem(0, primerObjeto);
                    player.updateInventory();
                }
            }
        } else if(plugin.hasProperty(player.getInventory().getItemInMainHand(), "Andanada")) {
            if(config.getStringList("Messages.blacklistedWorlds").contains(player.getWorld().getName())) {
                return;
            }

            if(!player.hasPermission("rareproperties.use.burst")) {
                return;
            }

            PlayerInventory in = player.getInventory();
            if(player.getInventory().getItemInMainHand() == null) {
                return;
            }

            if (!burst) {
                ItemStack hand = in.getItemInMainHand();
                level = comprobacionObjeto(hand, "Andanada");
                if (level > 0)
                    burst = true;
            }

            if (!burst) {
                return;
            }

            int coste = 11 - level;

            if(player.getFoodLevel() < coste) {
                return;
            }

            List<Entity> nearbyEntities = player.getNearbyEntities(8, 8, 8);
            Vector vPlayer = player.getLocation().toVector();

            for(Entity ent : nearbyEntities) {
                Vector unitVector = ent.getLocation().toVector().subtract(vPlayer).normalize();
                unitVector.setY(0.55/level);
                ent.setVelocity(unitVector.multiply(level*2));
            }

            player.setFoodLevel(player.getFoodLevel() - coste);
        } else if(plugin.hasProperty(player.getInventory().getItemInMainHand(), "Teletransporte")) {
            if(config.getStringList("Messages.blacklistedWorlds").contains(player.getWorld().getName())) {
                return;
            }

            if(!player.hasPermission("rareproperties.use.burst")) {
                return;
            }

            PlayerInventory in = player.getInventory();
            if(player.getInventory().getItemInMainHand() == null) {
                return;
            }

            if (!tp) {
                ItemStack hand = in.getItemInMainHand();
                level = comprobacionObjeto(hand, "Teletransporte");
                if (level > 0)
                    tp = true;
            }

            if (!tp) {
                return;
            }

            int coste = 1;

            if(player.getFoodLevel() < coste) {
                return;
            }

            int distance = 2 + 2 * level;

            Location playerLocation = player.getLocation();
            Vector direction = playerLocation.getDirection();

            direction.normalize();
            direction.multiply(distance);
            playerLocation.add(direction);

            if(playerLocation.getBlock().isEmpty() || playerLocation.getBlock().isPassable()) {
                if(playerLocation.add(0, 1, 0).getBlock().isEmpty() || playerLocation.add(0, 1, 0).getBlock().isPassable()) {
                    player.teleport(playerLocation);
                    player.setFoodLevel(player.getFoodLevel() - coste);
                    return;
                }
            }
            player.sendMessage(config.getString("Messages.cannotTeleport").replace("&", "§"));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMobDeath(EntityDeathEvent event) {
        boolean capture = false;
        try {
            Player p = event.getEntity().getKiller();
            if(p == null) {
                return;
            }
            LivingEntity entity = event.getEntity();

            if(p.getInventory().getItemInMainHand().getType() == Material.AIR) {
                return;
            } else if(p.getInventory().getItemInMainHand().getItemMeta() == null) {
                return;
            }

            if (!plugin.hasProperty(p.getInventory().getItemInMainHand(), "Captura")) {
                return;
            }

            if (!p.hasPermission("rareproperties.use.capture")) {
                return;
            }

            int level = 0;
            PlayerInventory in = p.getInventory();
            if (!capture) {
                ItemStack cursor = in.getItemInMainHand();
                level = comprobacionObjeto(cursor, "Captura");
                if (level > 0)
                    capture = true;
            }

            if (!capture) {
                return;
            }

            if (!plugin.getMobs().contains(entity.getType())) {
                return;
            }

            Random random = new Random();
            int prob = random.nextInt(10000);
            if (level < prob) {
                return;
            }

            String conversion = entity.getType().toString() + "_SPAWN_EGG";

            Material eggMaterial = Material.valueOf(conversion);

            ItemStack egg = new ItemStack(eggMaterial);
            egg.setAmount(1);
            p.sendMessage(config.getString("Messages.captureSuccess").replace("&", "§"));
            event.getDrops().add(egg);
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

    public int comprobacionObjeto(ItemStack is, String property) {
        ItemMeta meta = is.getItemMeta();
        int level = 0;

        NamespacedKey key = new NamespacedKey(plugin, Normalizer.normalize("RareProperties" + property, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
        if(meta == null) {
            return level;
        }

        PersistentDataContainer itemData = meta.getPersistentDataContainer();
        if(itemData.has(key, PersistentDataType.INTEGER)) {
            level = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
        }

        return level;
    }

    private ItemStack createItem(ItemStack item, List<String> lore) {
        ItemStack newItem = new ItemStack(item);
        ItemMeta meta = newItem.getItemMeta();
        meta.setLore(lore);
        newItem.setItemMeta(meta);
        return newItem;
    }
}
