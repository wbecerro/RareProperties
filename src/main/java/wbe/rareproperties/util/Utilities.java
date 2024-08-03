package wbe.rareproperties.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.items.SindrisFavour;
import wbe.rareproperties.properties.RareProperty;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utilities {

    private RareProperties plugin;

    private FileConfiguration config;

    public Utilities(RareProperties plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public ArrayList<String> getValid() {
        ArrayList<String> validProperties = new ArrayList<>();
        for(RareProperty property : plugin.getProperties()) {
            validProperties.add(property.getExternalName());
        }

        return validProperties;
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
            p.sendMessage(config.getString("Messages.alreadyHasProperty").replace("&", "§"));
            return;
        }

        String propertyLine = ("&" + color + property + " " + level).replace("&", "§");
        int propertyLevel = RomanToDecimal.romanToDecimal(level);
        lore.add(propertyLine);
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, Normalizer.normalize("RareProperties" + property, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, propertyLevel);

        item.setItemMeta(meta);
        p.sendMessage(config.getString("Messages.propertyAdded").replace("&", "§").replace("%property%", property));
    }

    public void removeProperty(ItemStack item, String property, Player p) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, Normalizer.normalize("RareProperties" + property, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));

        if(!hasProperty(item, property)) {
            p.sendMessage(config.getString("Messages.propertyNotPresent").replace("&", "§"));
            return;
        }

        int level = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);

        if(meta.hasLore()) {
            List<String> lore = meta.getLore();
            int line = getLine(lore, property);

            lore.remove(line);
            meta.getPersistentDataContainer().remove(key);

            meta.setLore(lore);
        }

        NamespacedKey limitKey = new NamespacedKey(plugin, "RarePropertiesLimit");
        if(meta.getPersistentDataContainer().has(limitKey)) {
            int limit = meta.getPersistentDataContainer().get(limitKey, PersistentDataType.INTEGER) - 1;
            if(limit < 1) {
                meta.getPersistentDataContainer().remove(limitKey);
            } else {
                meta.getPersistentDataContainer().set(limitKey, PersistentDataType.INTEGER, limit);
            }
        }

        item.setItemMeta(meta);

        if(getProperty(property) != null) {
            giveProperty(property, DecimalToRoman.intToRoman(level), p);
        }
        p.sendMessage(config.getString("Messages.propertyRemoved").replace("&", "§").replace("%property%", property));
    }

    public void giveProperty(String property, String level, Player p) {
        SindrisFavour sindrisFavour = new SindrisFavour(Material.valueOf(config.getString("SindrisFavour.material")), config);
        sindrisFavour.setProperty(getProperty(property), level, plugin);
        p.getInventory().addItem(new ItemStack[] { sindrisFavour });
        p.sendMessage(config.getString("Messages.propertyGiven").replace("&", "§").replace("%property%", property).replace("%level%", level));
        p.updateInventory();
    }

    public void giveRandomProperty(String property, Player p) {
        Random random = new Random();
        int lvl = random.nextInt(5) + 1;
        String level = DecimalToRoman.intToRoman(lvl);

        SindrisFavour sindrisFavour = new SindrisFavour(Material.valueOf(config.getString("SindrisFavour.material")), config);
        sindrisFavour.setProperty(getProperty(property), level, plugin);
        p.getInventory().addItem(new ItemStack[] { sindrisFavour });
        p.updateInventory();
    }

    public boolean hasProperty(ItemStack item, String property) {
        ItemMeta meta = item.getItemMeta();

        NamespacedKey key = new NamespacedKey(plugin, Normalizer.normalize("RareProperties" + property, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
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
        for(RareProperty rareProperty : plugin.getProperties()) {
            if(rareProperty.getExternalName().equalsIgnoreCase(property)) {
                return rareProperty;
            }
        }
        return null;
    }
}
