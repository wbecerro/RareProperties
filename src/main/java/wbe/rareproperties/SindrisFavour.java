package wbe.rareproperties;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        lore.add(ChatColor.GRAY + "tu objeto con la siguiente propiedad:");
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 10, true);
        meta.setLore(lore);
        setItemMeta(meta);
    }

    public void setProperty(String property, String level, boolean diablo) {
        ItemMeta meta = getItemMeta();
        List<String> lore;
        lore = meta.getLore();
        if(diablo) {
            lore.add(ChatColor.LIGHT_PURPLE + level + " " + property);
        } else {
            lore.add(ChatColor.LIGHT_PURPLE + property + " " + level);
        }
        meta.setLore(lore);
        setItemMeta(meta);
    }
}
