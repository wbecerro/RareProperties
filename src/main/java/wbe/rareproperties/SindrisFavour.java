package wbe.rareproperties;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.List;

public class SindrisFavour extends ItemStack {
    public SindrisFavour(Material mat) {
        super(mat);
        ItemMeta meta;
        if (hasItemMeta()) {
            meta = getItemMeta();
        } else {
            meta = Bukkit.getItemFactory().getItemMeta(mat);
        }
        setItemMeta(meta);

        meta = getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Favor de Sindri");
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.RESET + "");
        lore.add(ChatColor.GRAY + "Sindri te hará el favor de mejorar");
        lore.add(ChatColor.GRAY + "tu objeto con la propiedad mágica de");
        meta.addEnchant(Enchantment.POWER, 10, true);
        meta.setLore(lore);
        setItemMeta(meta);
    }

    public void setProperty(String property, String level, boolean diablo, RareProperties plugin) {
        ItemMeta meta = getItemMeta();
        List<String> lore;
        lore = meta.getLore();
        if(diablo) {
            lore.add(ChatColor.LIGHT_PURPLE + level + " " + property);
        } else {
            lore.add(ChatColor.LIGHT_PURPLE + property + " " + level);
        }
        meta.setLore(lore);
        NamespacedKey key = new NamespacedKey(plugin, "SindrisFavourProperty");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, property);
        key = new NamespacedKey(plugin, "SindrisFavourLevel");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, level);
        key = new NamespacedKey(plugin, "SindrisFavourDiablo");
        meta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, diablo);
        setItemMeta(meta);
    }
}
