package wbe.rareproperties.properties;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.mythic.Channeling;
import wbe.rareproperties.util.Utilities;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;

public abstract class RareProperty {

    private int level;

    private String name;

    private String externalName;

    private List<String> description;

    private RareProperties plugin;

    private FileConfiguration config;

    public Utilities utilities;

    public static HashMap<Player, Boolean> scaleModified = new HashMap<>();

    public RareProperty(RareProperties plugin, List<String> description, String name, String externalName) {
        this.name = name;
        this.externalName = externalName;
        this.description = description;
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
        this.utilities = new Utilities(plugin);
    }

    public RareProperty(int level, RareProperties plugin, List<String> description, String name, String externalName) {
        this.level = level;
        this.name = name;
        this.externalName = externalName;
        this.description = description;
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
        this.utilities = new Utilities(plugin);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public RareProperties getPlugin() {
        return plugin;
    }

    public void setPlugin(RareProperties plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    public abstract void applyEffect(Player player, Event event);

    public abstract boolean checkUse(Player player, Event event);

    public int checkUseHands(Player player) {
        int level = -1;

        if(!utilities.hasProperty(player.getInventory().getItemInMainHand(), getExternalName())) {
            if(!utilities.hasProperty(player.getInventory().getItemInOffHand(), getExternalName())) {
                return level;
            }
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        level = checkProperty(item, getExternalName());

        if(level < 0) {
            item = inventory.getItemInOffHand();
            level = checkProperty(item, getExternalName());
        }

        return level;
    }

    public int checkUseArmor(Player player) {
        int level = -1;

        PlayerInventory inventory = player.getInventory();
        level = checkArmorAccumulative(inventory, getExternalName());

        return level;
    }

    public int checkProperty(ItemStack is, String property) {
        ItemMeta meta = is.getItemMeta();
        int level = -1;

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

    public int checkHands(PlayerInventory inventory, String property) {
        ItemStack hand = inventory.getItemInMainHand();
        level = checkProperty(hand, property);
        if(level > 0) {
            return level;
        } else {
            ItemStack offHand = inventory.getItemInOffHand();
            level = checkProperty(offHand, property);
            if(level > 0) {
                return level;
            }
        }

        return -1;
    }

    public int checkArmor(PlayerInventory inventory, String property) {
        ItemStack[] armor = inventory.getArmorContents();
        for(ItemStack item : armor) {
            if(item == null) {
                continue;
            }

            level = checkProperty(item, property);
            if(level > 0) {
                return level;
            }
        }

        return -1;
    }


    public int checkArmorAccumulative(PlayerInventory inventory, String property) {
        ItemStack[] armor = inventory.getArmorContents();
        int level = 0;
        int itemLevel = -1;
        for(ItemStack item : armor) {
            if(item == null) {
                continue;
            }

            itemLevel = checkProperty(item, property);
            if(itemLevel > 0) {
                level += itemLevel;
            }
        }

        return level > 0 ? level : -1;
    }

    public boolean checkFoodCost(Player player, int cost) {
        return utilities.checkFoodCost(player, cost);
    }

    public boolean applyFoodCost(Player player, int cost) {
        return utilities.applyFoodCost(player, cost);
    }

    public boolean checkHealthCost(Player player, int cost) {
        return player.getHealth() > cost;
    }

    public boolean applyHealthCost(Player player, int cost) {
        if(!checkHealthCost(player, cost)) {
            player.sendMessage(RareProperties.messages.notEnoughHealth);
            return false;
        }

        player.setHealth(player.getHealth() - cost);
        return true;
    }
}
