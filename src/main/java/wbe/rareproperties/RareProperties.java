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
import wbe.rareproperties.properties.*;

import java.text.Normalizer;
import java.util.*;

public class RareProperties extends JavaPlugin implements Listener {

    private final CommandListener commandListener = new CommandListener(this);

    private final EventListeners eventListeners = new EventListeners(this);

    private final ArrayList<RareProperty> properties = new ArrayList<>(Arrays.asList(new Fly(this), new Repair(this), new Burst(this),
            new Capture(this), new Teleport(this), new Reinforced(this), new Aegis(this), new Demolition(this), new Promptness(this),
            new Propulsion(this)));

    private final ArrayList<EntityType> mobs = new ArrayList<EntityType>(Arrays.asList(EntityType.ALLAY, EntityType.ARMADILLO, EntityType.AXOLOTL, EntityType.BAT,
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

    public ArrayList<String> getValid() {
        ArrayList<String> validProperties = new ArrayList<>();
        for(RareProperty property : properties) {
            validProperties.add(property.getExternalName());
        }

        return validProperties;
    }

    public ArrayList<EntityType> getMobs() {
        return mobs;
    }

    public void addProperty(ItemStack item, String property, String level, String color, Player p) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore;

        if(!meta.hasLore()) {
            lore = new ArrayList<>();
        } else {
            lore = meta.getLore();
        }

        if(hasProperty(item, property)) {
            p.sendMessage(this.getConfig().getString("Messages.alreadyHasProperty").replace("&", "§"));
            return;
        }

        String propertyLine = ("&" + color + property + " " + level).replace("&", "§");
        int propertyLevel = RomanToDecimal.romanToDecimal(level);
        lore.add(propertyLine);
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(this, Normalizer.normalize("RareProperties" + property, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, propertyLevel);

        item.setItemMeta(meta);
        p.sendMessage(this.getConfig().getString("Messages.propertyAdded").replace("&", "§").replace("%property%", property));
    }

    public void removeProperty(ItemStack item, String property, Player p) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(this, Normalizer.normalize("RareProperties" + property, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));

        if(!hasProperty(item, property)) {
            p.sendMessage(this.getConfig().getString("Messages.propertyNotPresent").replace("&", "§"));
            return;
        }

        int level = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);

        if(meta.hasLore()) {
            List<String> lore = meta.getLore();
            int line = getLine(lore, property);

            lore.remove(line);
            meta.getPersistentDataContainer().remove(key);

            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        if(getProperty(property) != null) {
            giveProperty(property, DecimalToRoman.intToRoman(level), p);
        }
        p.sendMessage(this.getConfig().getString("Messages.propertyRemoved").replace("&", "§").replace("%property%", property));
    }

    public void giveProperty(String property, String level, Player p) {
        SindrisFavour sindrisFavour = new SindrisFavour(Material.valueOf(this.getConfig().getString("Messages.upgradeMaterial")));
        sindrisFavour.setProperty(getProperty(property), level, this);
        p.getInventory().addItem(new ItemStack[] { sindrisFavour });
        p.sendMessage(this.getConfig().getString("Messages.propertyGiven").replace("&", "§").replace("%property%", property).replace("%level%", level));
        p.updateInventory();
    }

    public void giveRandomProperty(String property, Player p) {
        Random random = new Random();
        int lvl = random.nextInt(5) + 1;
        String level = DecimalToRoman.intToRoman(lvl);

        SindrisFavour sindrisFavour = new SindrisFavour(Material.valueOf(this.getConfig().getString("Messages.upgradeMaterial")));
        sindrisFavour.setProperty(getProperty(property), level, this);
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

    public int getLine(List<String> lore, String property) {
        int size = lore.size();

        for (int i = 0; i < size; i++) {
            String loreLine = lore.get(i).toString();
            if (loreLine.contains(property)) {
                return i;
            }
        }

        return -1;
    }

    public RareProperty getProperty(String property) {
        for(RareProperty rareProperty : properties) {
            if(rareProperty.getExternalName().equalsIgnoreCase(property)) {
                return rareProperty;
            }
        }
        return null;
    }
}

