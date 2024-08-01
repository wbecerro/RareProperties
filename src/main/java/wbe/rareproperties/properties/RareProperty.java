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
import java.util.List;

public abstract class RareProperty {

    private int level;

    private String name;

    private String externalName;

    private List<String> description;

    private RareProperties plugin;

    private FileConfiguration config;

    public RareProperty(RareProperties plugin, List<String> description, String name, String externalName) {
        this.name = name;
        this.externalName = externalName;
        this.description = description;
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    public RareProperty(int level, RareProperties plugin, List<String> description, String name, String externalName) {
        this.level = level;
        this.name = name;
        this.externalName = externalName;
        this.description = description;
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
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
