package wbe.rareproperties.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.RareProperty;

import java.util.ArrayList;
import java.util.List;

public class SindrisFavour extends ItemStack {

    FileConfiguration config;

    public SindrisFavour(Material mat, FileConfiguration config) {
        super(mat);
        this.config = config;

        ItemMeta meta;
        if (hasItemMeta()) {
            meta = getItemMeta();
        } else {
            meta = Bukkit.getItemFactory().getItemMeta(mat);
        }
        setItemMeta(meta);

        meta = getItemMeta();
        meta.setDisplayName(config.getString("SindrisFavour.name").replace("&", "§"));

        ArrayList<String> lore = new ArrayList<>();
        for(String line : config.getStringList("SindrisFavour.lore")) {
            lore.add(line.replace("&", "§"));
        }

        meta.addEnchant(Enchantment.POWER, 10, true);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        setItemMeta(meta);
    }

    public void setProperty(RareProperty property, String level, RareProperties plugin) {
        ItemMeta meta = getItemMeta();
        List<String> lore;
        lore = meta.getLore();
        lore.add(config.getString("SindrisFavour.property").replace("&", "§")
                .replace("%property%", property.getExternalName()).replace("%level%", level));

        for(String description : property.getDescription()) {
            lore.add(description.replace("&", "§"));
        }
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, "SindrisFavourProperty");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, property.getExternalName());
        key = new NamespacedKey(plugin, "SindrisFavourLevel");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, level);

        setItemMeta(meta);
    }
}
