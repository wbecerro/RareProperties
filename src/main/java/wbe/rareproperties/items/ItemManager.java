package wbe.rareproperties.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.rarities.ItemRarity;
import wbe.rareproperties.rarities.PropertyRarity;
import wbe.rareproperties.util.Utilities;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ItemManager {

    RareProperties plugin;

    Utilities utilities;

    public ItemManager(RareProperties plugin) {
        this.plugin = plugin;
        this.utilities = new Utilities(plugin);
    }

    public ItemStack generateItem(boolean armor) {
        Random random = new Random();
        if(random.nextInt(100) < RareProperties.config.sockettedItemChance) {
            return generateSockettedItem(armor);
        }

        if(random.nextInt(100) < RareProperties.config.unidentifiedChance) {
            return generateUnidentifiedItem(armor);
        }

        return generateRandomItem(armor);
    }

    public ItemStack generateSockettedItem(boolean armor) {
        ItemStack randomItem = generateBaseItem(armor);
        return null;
    }

    public ItemStack generateUnidentifiedItem(boolean armor) {
        return null;
    }

    public ItemStack generateRandomItem(boolean armor) {
        ItemStack baseItem = generateBaseItem(armor);
        ItemRarity rarity = utilities.calculateItemRarity();
        String prefix = utilities.getRandomPrefix();
        String suffix = utilities.getRandomSuffix();

        Bukkit.getServer().broadcastMessage(rarity.getColor() + " " + rarity.getMaxEnchantLevel() + " " + rarity.getMaxEnchants() + " " + rarity.getMaxProperties());

        int maxProperties = rarity.getMaxProperties();
        if(maxProperties != 0) {
            Random random = new Random();
            Bukkit.getServer().broadcastMessage("Calculando propiedades");
            for(int i=0;i<maxProperties;i++) {
                PropertyRarity propertyRarity = utilities.calculatePropertyRarity();
                String property = propertyRarity.getProperties().get(
                        random.nextInt(propertyRarity.getProperties().size()));
                Bukkit.getServer().broadcastMessage(propertyRarity.getName() + " " + property);
                utilities.addProperty(baseItem, property, random.nextInt(5) + 1, propertyRarity.getColor());
            }
        }

        int maxEnchantments = rarity.getMaxEnchants();
        Random random = new Random();
        for(int i=0;i<maxEnchantments;i++) {
            Enchantment enchantment = utilities.getRandomEnchantment();
            int level = random.nextInt(rarity.getMaxEnchantLevel()) + 1;
            Bukkit.getServer().broadcastMessage(enchantment.toString() + " " + level);
            baseItem.addUnsafeEnchantment(enchantment, level);
        }

        ItemMeta meta = baseItem.getItemMeta();
        meta.setDisplayName((rarity.getColor() + prefix + " " + suffix).replace("&", "§"));
        baseItem.setItemMeta(meta);

        return baseItem;
    }

    private ItemStack generateBaseItem(boolean armor) {
        Material material = Material.AIR;
        Random random = new Random();
        if(armor) {
            material = Material.valueOf(RareProperties.config.armorMaterials.get(
                    random.nextInt(RareProperties.config.armorMaterials.size())));
        } else {
            material = Material.valueOf(RareProperties.config.weaponMaterials.get(
                    random.nextInt(RareProperties.config.weaponMaterials.size())));
        }

        ItemStack baseItem = new ItemStack(material);

        if(!baseItem.hasItemMeta()) {
            baseItem.setItemMeta(Bukkit.getItemFactory().getItemMeta(material));
        }

        return baseItem;
    }
}