package wbe.rareproperties.properties;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import wbe.rareproperties.RareProperties;

import java.text.Normalizer;

public abstract class RareProperty {

    private int level;

    private RareProperties plugin;

    private FileConfiguration config;

    public RareProperty(RareProperties plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    public RareProperty(int level, RareProperties plugin) {
        this.level = level;
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public int checkProperty(ItemStack is, String property) {
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
}
