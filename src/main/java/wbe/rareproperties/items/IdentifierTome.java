package wbe.rareproperties.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import wbe.rareproperties.RareProperties;

import java.util.ArrayList;

public class IdentifierTome extends ItemStack {

    public IdentifierTome() {
        super(RareProperties.config.identifierMaterial);

        ItemMeta meta;
        if(hasItemMeta()) {
            meta = getItemMeta();
        } else {
            meta = Bukkit.getItemFactory().getItemMeta(RareProperties.config.identifierMaterial);
        }

        meta.setDisplayName(RareProperties.config.identifierName);

        ArrayList<String> lore = new ArrayList<>();
        for(String line : RareProperties.config.identifierLore) {
            lore.add(line.replace("&", "§"));
        }

        meta.setLore(lore);
        setItemMeta(meta);

        BookMeta bookMeta = (BookMeta) getItemMeta();
        bookMeta.setAuthor(ChatColor.MAGIC + RareProperties.config.identifierAuthor);
        setItemMeta(bookMeta);
    }
}
