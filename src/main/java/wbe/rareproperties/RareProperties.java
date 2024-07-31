package wbe.rareproperties;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import wbe.rareproperties.properties.Fly;

import java.text.Normalizer;
import java.util.*;

public class RareProperties extends JavaPlugin implements Listener {

    private final CommandListener commandListener = new CommandListener(this);

    private final EventListeners eventListeners = new EventListeners(this);

    ArrayList<String> tipos = new ArrayList<String>(Arrays.asList("Vuelo", "Reparación", "Andanada", "Captura", "Teletransporte",
            "Reforzado", "Égida", "Demolición", "Presteza", "Propulsión", "Ataque", "Encogimiento", "Solem", "Noctis", "Electro",
            "Vampirismo", "Invocador", "Puñalada", "Fuego", "Explosión", "Armadura", "Invisibility", "Speed", "Regeneration",
            "Resistance", "Night_Vision", "Jump", "Congelamiento", "Crítico"));

    ArrayList<String> validos = new ArrayList<String>(Arrays.asList("Vuelo", "Reparación", "Andanada", "Captura", "Teletransporte"
            , "Reforzado", "Égida", "Demolición", "Presteza", "Propulsión"));

    ArrayList<EntityType> mobs = new ArrayList<EntityType>(Arrays.asList(EntityType.ALLAY, EntityType.ARMADILLO, EntityType.AXOLOTL, EntityType.BAT,
            EntityType.BEE, EntityType.BLAZE, EntityType.BOGGED, EntityType.BREEZE, EntityType.CAMEL, EntityType.CAT, EntityType.CAVE_SPIDER,
            EntityType.CHICKEN, EntityType.COD, EntityType.COW, EntityType.CREEPER, EntityType.DOLPHIN, EntityType.DONKEY, EntityType.DROWNED,
            EntityType.ELDER_GUARDIAN, EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.EVOKER, EntityType.FOX, EntityType.FROG, EntityType.GHAST,
            EntityType.GLOW_SQUID, EntityType.GOAT, EntityType.GUARDIAN, EntityType.HOGLIN, EntityType.HORSE, EntityType.HUSK,
            EntityType.IRON_GOLEM, EntityType.LLAMA, EntityType.MAGMA_CUBE, EntityType.MOOSHROOM, EntityType.MULE, EntityType.OCELOT,
            EntityType.PANDA, EntityType.PARROT, EntityType.PHANTOM, EntityType.PIG, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE,
            EntityType.PILLAGER, EntityType.POLAR_BEAR, EntityType.PUFFERFISH, EntityType.RABBIT, EntityType.RAVAGER,
            EntityType.SALMON, EntityType.SHEEP, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SKELETON_HORSE,
            EntityType.SLIME, EntityType.SNIFFER, EntityType.SNOW_GOLEM, EntityType.SPIDER, EntityType.SQUID, EntityType.STRAY, EntityType.STRIDER,
            EntityType.TADPOLE, EntityType.TRADER_LLAMA, EntityType.TROPICAL_FISH, EntityType.TURTLE, EntityType.VEX, EntityType.VILLAGER,
            EntityType.VINDICATOR, EntityType.WANDERING_TRADER, EntityType.WARDEN, EntityType.WITCH, EntityType.WITHER_SKELETON, EntityType.WOLF,
            EntityType.ZOGLIN, EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN));

    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("RareProperties enabled correctly");
        getCommand("rareproperties").setExecutor(this.commandListener);
        getServer().getPluginManager().registerEvents(this.eventListeners, (Plugin) this);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Set<Player> keys = Fly.playersFlying.keySet();
                for(Player player : keys) {
                    if(player.isFlying()) {
                        int level = Fly.playersFlying.get(player);
                        int rest = (int)(getConfig().getDouble("Properties.Fly.baseCost") - level);
                        final int food = rest;
                        player.setFoodLevel(player.getFoodLevel() - food);
                    }

                    if(player.getFoodLevel() <= 0) {
                        player.setFlying(false);
                    }
                }
            }
        }, 10L, getConfig().getLong("Properties.Fly.time") * 20);
    }

    public void onDisable() {
        reloadConfig();
        getLogger().info("RareProperties disabled correctly");
    }

    public ArrayList<String> getTipos() {
        return tipos;
    }

    public ArrayList<EntityType> getMobs() {
        return mobs;
    }

    public void addProperty(ItemStack item, String property, String level, String color, Player p, boolean diablo) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore;

        if(!meta.hasLore()) {
            lore = new ArrayList<String>();
        } else {
            lore = meta.getLore();
        }

        if(hasProperty(item, property)) {
            p.sendMessage(this.getConfig().getString("Messages.alreadyHasProperty").replace("&", "§"));
            return;
        }

        String propertyLine;
        int propertyLevel = 0;
        if(diablo) {
            propertyLine = ("&" + color + level + " " + property).replace("&", "§");
            propertyLevel = Integer.parseInt(level);
        } else {
            propertyLine = ("&" + color + property + " " + level).replace("&", "§");
            propertyLevel = RomanToDecimal.romanToDecimal(level);
        }
        lore.add(propertyLine);

        p.sendMessage(this.getConfig().getString("Messages.propertyAdded").replace("&", "§").replace("%property%", property));
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(this, Normalizer.normalize("RareProperties" + property, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, propertyLevel);

        item.setItemMeta(meta);
    }

    public void removeProperty(ItemStack item, String property, Player p) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(this, Normalizer.normalize("RareProperties" + property, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));

        if(!tipos.contains(property)) {
            p.sendMessage(this.getConfig().getString("Messages.propertyNotPresent").replace("&", "§"));
            return;
        }

        if(!meta.hasLore()) {
            p.sendMessage(this.getConfig().getString("Messages.propertyNotPresent").replace("&", "§"));
            return;
        }

        List<String> lore = meta.getLore();
        int len = lore.size();
        int line = -1;

        for (int i = 0; i < len; i++) {
            String loreLine = lore.get(i).toString();
            if (loreLine.contains(property)) {
                line = i;
                break;
            }
        }

        if(line == -1) {
            p.sendMessage(this.getConfig().getString("Messages.propertyNotPresent").replace("&", "§"));
            return;
        }

        int level = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);

        String[] levelLine = lore.get(line).split(" ");
        String color = levelLine[0].substring(1, 2);

        if(validos.contains(property)) {
            giveProperty(property, String.valueOf(level), p, false);
        } else {
            if(color.equalsIgnoreCase("d")) {
                ItemStack dragonEgg = new ItemStack(Material.DRAGON_EGG);
                ItemMeta dragonEggMeta = dragonEgg.getItemMeta();
                dragonEggMeta.setDisplayName("§d§lHuevo de Dragón");
                ArrayList<String> dragonEggLore = new ArrayList<>(Arrays.asList("§d", "§7Huevo con la esencia de un dragón", "§7legendario extinto hace mucho tiempo."));
                dragonEggMeta.setLore(dragonEggLore);
                dragonEgg.setItemMeta(dragonEggMeta);
                if(p.getInventory().firstEmpty() == -1) {
                    p.getWorld().dropItem(p.getLocation(), dragonEgg);
                } else {
                    p.getInventory().addItem(dragonEgg);
                }
            }
        }

        lore.remove(line);
        meta.getPersistentDataContainer().remove(key);

        p.sendMessage(this.getConfig().getString("Messages.propertyRemoved").replace("&", "§").replace("%property%", property));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public void giveProperty(String property, String level, Player p, boolean diablo) {
        SindrisFavour sindrisFavour = new SindrisFavour(Material.valueOf(this.getConfig().getString("Messages.upgradeMaterial")));
        sindrisFavour.setProperty(property, level, diablo, this);
        p.getInventory().addItem(new ItemStack[] { sindrisFavour });
        p.sendMessage(this.getConfig().getString("Messages.propertyGiven").replace("&", "§").replace("%property%", property).replace("%level%", level));
        p.updateInventory();
    }

    public void giveRandomProperty(String property, Player p) {
        Random random = new Random();
        int lvl = random.nextInt(5) + 1;
        String level = decimalToRoman(lvl);

        SindrisFavour sindrisFavour = new SindrisFavour(Material.valueOf(this.getConfig().getString("Messages.upgradeMaterial")));
        sindrisFavour.setProperty(property, level, false, this);
        p.getInventory().addItem(new ItemStack[] { sindrisFavour });
        p.updateInventory();
    }

    public void giveRandomDiabloProperty(String property, String max, Player p) {
        int maximum = Integer.valueOf(max);

        Random random = new Random();
        int lvl = random.nextInt(maximum) + 1;

        String level;
        level = "+" + String.valueOf(lvl);

        SindrisFavour sindrisFavour = new SindrisFavour(Material.valueOf(this.getConfig().getString("Messages.upgradeMaterial")));
        sindrisFavour.setProperty(property, level, true, this);
        p.getInventory().addItem(new ItemStack[] { sindrisFavour });
        p.updateInventory();
    }

    public boolean hasProperty(ItemStack item, String property) {
        ItemMeta meta = item.getItemMeta();

        NamespacedKey key = new NamespacedKey(this, Normalizer.normalize("RareProperties" + property, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
        if(meta == null) {
            return false;
        }

        PersistentDataContainer itemData = meta.getPersistentDataContainer();
        if(itemData.has(key, PersistentDataType.INTEGER)) {
            return true;
        }

        return false;
    }

    private String decimalToRoman(int decimal) {
        switch(decimal) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
        }

        return "";
    }
}

