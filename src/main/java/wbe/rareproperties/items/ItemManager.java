package wbe.rareproperties.items;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.rarities.ItemRarity;
import wbe.rareproperties.rarities.PropertyRarity;
import wbe.rareproperties.util.Utilities;

import java.util.ArrayList;
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
        int randomNumber = random.nextInt(100);
        if(randomNumber < RareProperties.config.sockettedItemChance) {
            return generateSockettedItem(armor);
        }

        if(randomNumber < RareProperties.config.unidentifiedChance + RareProperties.config.sockettedItemChance) {
            return generateUnidentifiedItem(armor);
        }

        return generateRandomItem(armor);
    }

    public ItemStack generateSockettedItem(boolean armor) {
        ItemStack baseItem = generateBaseItem(armor);
        ItemRarity rarity = utilities.calculateItemRarity();
        String prefix = utilities.getRandomPrefix();
        String suffix = utilities.getRandomSuffix();
        Random random = new Random();

        // Cálculo de encantamientos
        int maxEnchantments = random.nextInt(rarity.getMaxEnchants() - rarity.getMinEnchants())
                + rarity.getMinEnchants();
        for(int i=0;i<maxEnchantments;i++) {
            Enchantment enchantment = utilities.getRandomEnchantment();
            int level = random.nextInt(rarity.getMaxEnchantLevel()) + 1;
            baseItem.addUnsafeEnchantment(enchantment, level);
        }

        ItemMeta meta = baseItem.getItemMeta();
        meta.setDisplayName((rarity.getColor() + prefix + " " + suffix).replace("&", "§"));

        List<String> lore;
        if(!meta.hasLore()) {
            lore = new ArrayList<>();
        } else {
            lore = meta.getLore();
        }

        int socketSlots = random.nextInt(RareProperties.config.maxSockets) + 1;
        StringBuilder loreLine = new StringBuilder();
        NamespacedKey colorsKey = new NamespacedKey(plugin, "rarepropertiessocketslots");
        StringBuilder colors = new StringBuilder();
        for(int i=0;i<socketSlots;i++) {
            String color = RareProperties.config.socketColors.get(random.nextInt(RareProperties.config.socketColors.size()));
            colors.append(color + ".");
            loreLine.append(ChatColor.valueOf(color) + RareProperties.config.socketSlot + " ");
        }
        lore.add("");
        lore.add(RareProperties.config.socketTitle);
        lore.add(loreLine.toString());
        meta.getPersistentDataContainer().set(colorsKey, PersistentDataType.STRING,
                colors.toString().substring(0, colors.toString().length() - 1));
        meta.setLore(lore);

        NamespacedKey limitKey = new NamespacedKey(plugin, "RarePropertiesLimit");
        meta.getPersistentDataContainer().set(limitKey, PersistentDataType.INTEGER, socketSlots);
        baseItem.setItemMeta(meta);

        Damageable damageable = (Damageable) baseItem.getItemMeta();
        int maxDurability = baseItem.getType().getMaxDurability();
        damageable.setDamage(random.nextInt(maxDurability) + 1);
        baseItem.setItemMeta(damageable);

        return baseItem;
    }

    public ItemStack generateUnidentifiedItem(boolean armor) {
        ItemStack baseItem = generateBaseItem(armor);
        String prefix = utilities.getRandomPrefix();
        String suffix = utilities.getRandomSuffix();

        ItemMeta meta = baseItem.getItemMeta();
        meta.setDisplayName(ChatColor.MAGIC + prefix + " " + suffix);

        List<String> lore;
        if(!meta.hasLore()) {
            lore = new ArrayList<>();
        } else {
            lore = meta.getLore();
        }

        lore.add(RareProperties.config.unidentifiedLore);
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, "rarepropertiesunidentified");
        meta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        baseItem.setItemMeta(meta);

        Random random = new Random();
        Damageable damageable = (Damageable) baseItem.getItemMeta();
        int maxDurability = baseItem.getType().getMaxDurability();
        damageable.setDamage(random.nextInt(maxDurability) + 1);
        baseItem.setItemMeta(damageable);

        return baseItem;
    }

    public ItemStack generateRandomItem(boolean armor) {
        ItemStack baseItem = generateBaseItem(armor);
        ItemRarity rarity = utilities.calculateItemRarity();
        String prefix = utilities.getRandomPrefix();
        String suffix = utilities.getRandomSuffix();
        Random random = new Random();

        // Cálculo de propiedades
        int maxProperties = random.nextInt(rarity.getMaxProperties() + 1);
        if(maxProperties != 0) {
            for(int i=0;i<maxProperties;i++) {
                PropertyRarity propertyRarity = utilities.calculatePropertyRarity();
                String property = propertyRarity.getProperties().get(
                        random.nextInt(propertyRarity.getProperties().size()));
                utilities.addProperty(baseItem, property, random.nextInt(5) + 1, propertyRarity.getColor(),
                        false);
            }
        }

        // Cálculo de encantamientos
        int maxEnchantments = random.nextInt(rarity.getMaxEnchants() - rarity.getMinEnchants())
                + rarity.getMinEnchants();
        for(int i=0;i<maxEnchantments;i++) {
            Enchantment enchantment = utilities.getRandomEnchantment();
            int level = random.nextInt(rarity.getMaxEnchantLevel()) + 1;
            baseItem.addUnsafeEnchantment(enchantment, level);
        }

        ItemMeta meta = baseItem.getItemMeta();
        meta.setDisplayName((rarity.getColor() + prefix + " " + suffix).replace("&", "§"));
        baseItem.setItemMeta(meta);

        Damageable damageable = (Damageable) baseItem.getItemMeta();
        int maxDurability = baseItem.getType().getMaxDurability();
        damageable.setDamage(random.nextInt(maxDurability) + 1);
        baseItem.setItemMeta(damageable);

        return baseItem;
    }

    public ItemStack generateRandomItem(Material material) {
        ItemStack baseItem = generateBaseItem(material);
        ItemRarity rarity = utilities.calculateItemRarity();
        String prefix = utilities.getRandomPrefix();
        String suffix = utilities.getRandomSuffix();
        Random random = new Random();

        // Cálculo de propiedades
        int maxProperties = random.nextInt(rarity.getMaxProperties() + 1);
        if(maxProperties != 0) {
            for(int i=0;i<maxProperties;i++) {
                PropertyRarity propertyRarity = utilities.calculatePropertyRarity();
                String property = propertyRarity.getProperties().get(
                        random.nextInt(propertyRarity.getProperties().size()));
                utilities.addProperty(baseItem, property, random.nextInt(5) + 1, propertyRarity.getColor(),
                        false);
            }
        }

        // Cálculo de encantamientos
        int maxEnchantments = random.nextInt(rarity.getMaxEnchants() - rarity.getMinEnchants())
                + rarity.getMinEnchants();
        for(int i=0;i<maxEnchantments;i++) {
            Enchantment enchantment = utilities.getRandomEnchantment();
            int level = random.nextInt(rarity.getMaxEnchantLevel()) + 1;
            baseItem.addUnsafeEnchantment(enchantment, level);
        }

        ItemMeta meta = baseItem.getItemMeta();
        meta.setDisplayName((rarity.getColor() + prefix + " " + suffix).replace("&", "§"));
        baseItem.setItemMeta(meta);

        Damageable damageable = (Damageable) baseItem.getItemMeta();
        int maxDurability = baseItem.getType().getMaxDurability();
        damageable.setDamage(random.nextInt(maxDurability) + 1);
        baseItem.setItemMeta(damageable);

        return baseItem;
    }

    public ItemStack generateRandomItem(boolean armor, String rarityString) {
        ItemStack baseItem = generateBaseItem(armor);
        ItemRarity rarity = RareProperties.config.getRarityFromName(rarityString);
        String prefix = utilities.getRandomPrefix();
        String suffix = utilities.getRandomSuffix();
        Random random = new Random();

        // Cálculo de propiedades
        int maxProperties = random.nextInt(rarity.getMaxProperties() + 1);
        if(maxProperties != 0) {
            for(int i=0;i<maxProperties;i++) {
                PropertyRarity propertyRarity = utilities.calculatePropertyRarity();
                String property = propertyRarity.getProperties().get(
                        random.nextInt(propertyRarity.getProperties().size()));
                utilities.addProperty(baseItem, property, random.nextInt(5) + 1, propertyRarity.getColor(),
                        false);
            }
        }

        // Cálculo de encantamientos
        int maxEnchantments = random.nextInt(rarity.getMaxEnchants() - rarity.getMinEnchants())
                + rarity.getMinEnchants();
        for(int i=0;i<maxEnchantments;i++) {
            Enchantment enchantment = utilities.getRandomEnchantment();
            int level = random.nextInt(rarity.getMaxEnchantLevel()) + 1;
            baseItem.addUnsafeEnchantment(enchantment, level);
        }

        ItemMeta meta = baseItem.getItemMeta();
        meta.setDisplayName((rarity.getColor() + prefix + " " + suffix).replace("&", "§"));
        baseItem.setItemMeta(meta);

        Damageable damageable = (Damageable) baseItem.getItemMeta();
        int maxDurability = baseItem.getType().getMaxDurability();
        damageable.setDamage(random.nextInt(maxDurability) + 1);
        baseItem.setItemMeta(damageable);

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

    private ItemStack generateBaseItem(Material material) {
        ItemStack baseItem = new ItemStack(material);

        if(!baseItem.hasItemMeta()) {
            baseItem.setItemMeta(Bukkit.getItemFactory().getItemMeta(material));
        }

        return baseItem;
    }
}