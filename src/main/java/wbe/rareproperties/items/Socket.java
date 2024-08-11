package wbe.rareproperties.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.rareproperties.RareProperties;

import java.util.ArrayList;
import java.util.Random;

public class Socket extends ItemStack {

    public Socket(RareProperties plugin) {
        super(RareProperties.config.socketMaterial);

        ItemMeta meta;
        if(hasItemMeta()) {
            meta = getItemMeta();
        } else {
            meta = Bukkit.getItemFactory().getItemMeta(RareProperties.config.socketMaterial);
        }

        Random random = new Random();
        String color = RareProperties.config.socketColors.get(random.nextInt(RareProperties.config.socketColors.size()));

        meta.setDisplayName(ChatColor.valueOf(color) + RareProperties.config.socketName);

        ArrayList<String> lore = new ArrayList<>();
        for(String line : RareProperties.config.socketLore) {
            lore.add(line.replace("&", "§"));
        }

        meta.addEnchant(Enchantment.POWER, 5, true);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        NamespacedKey colorKey = new NamespacedKey(plugin, "Socket");
        meta.getPersistentDataContainer().set(colorKey, PersistentDataType.STRING, color);

        setItemMeta(meta);
    }

    public Socket(RareProperties plugin, String color) {
        super(RareProperties.config.socketMaterial);

        ItemMeta meta;
        if(hasItemMeta()) {
            meta = getItemMeta();
        } else {
            meta = Bukkit.getItemFactory().getItemMeta(RareProperties.config.socketMaterial);
        }

        meta.setDisplayName(ChatColor.valueOf(color) + RareProperties.config.socketName);

        ArrayList<String> lore = new ArrayList<>();
        for(String line : RareProperties.config.socketLore) {
            lore.add(line.replace("&", "§"));
        }

        meta.addEnchant(Enchantment.POWER, 5, true);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        NamespacedKey colorKey = new NamespacedKey(plugin, "Socket");
        meta.getPersistentDataContainer().set(colorKey, PersistentDataType.STRING, color);

        setItemMeta(meta);
    }
}
