package wbe.rareproperties.items;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.rareproperties.RareProperties;

import java.util.ArrayList;

public class SpecialCraftingItem extends ItemStack {

    public SpecialCraftingItem(RareProperties plugin) {
        super(RareProperties.config.specialCraftingItemMaterial);

        ItemMeta meta;
        if (hasItemMeta()) {
            meta = getItemMeta();
        } else {
            meta = Bukkit.getItemFactory().getItemMeta(RareProperties.config.specialCraftingItemMaterial);
        }

        meta.setDisplayName(RareProperties.config.specialCraftingItemName);

        ArrayList<String> lore = new ArrayList<>();
        for(String line : RareProperties.config.specialCraftingItemLore) {
            lore.add(line.replace("&", "§"));
        }

        meta.addEnchant(Enchantment.INFINITY, 1, true);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        NamespacedKey key = new NamespacedKey(plugin, "SpecialCraftingItem");
        meta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);

        setItemMeta(meta);
    }
}
