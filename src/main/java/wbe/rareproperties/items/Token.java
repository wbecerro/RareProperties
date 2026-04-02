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
import wbe.rareproperties.rarities.ItemRarity;

import java.util.ArrayList;

public class Token extends ItemStack {

    public Token(RareProperties plugin, ItemRarity rarity) {
        super(rarity.getTokenMaterial());

        ItemMeta meta;
        if(hasItemMeta()) {
            meta = getItemMeta();
        } else {
            meta = Bukkit.getItemFactory().getItemMeta(rarity.getTokenMaterial());
        }

        meta.setDisplayName(RareProperties.config.tokenName
                .replace("%color%", rarity.getColor())
                .replace("%rarity%", rarity.getName())
                .replace("&", "§"));

        ArrayList<String> lore = new ArrayList<>();
        for(String line : RareProperties.config.tokenLore) {
            lore.add(line.replace("&", "§"));
        }

        meta.addEnchant(Enchantment.INFINITY, 1, true);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        NamespacedKey colorKey = new NamespacedKey(plugin, "rarity");
        meta.getPersistentDataContainer().set(colorKey, PersistentDataType.STRING, rarity.getId());

        setItemMeta(meta);
    }
}
