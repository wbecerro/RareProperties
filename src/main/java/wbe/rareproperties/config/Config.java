package wbe.rareproperties.config;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.rarities.ItemRarity;
import wbe.rareproperties.rarities.PropertyRarity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Config {

    private FileConfiguration config;

    public int specialMobChance;
    public int sockettedItemChance;
    public int unidentifiedChance;
    public int maxSockets;
    public int socketChance;
    public int tomeChance;
    public double lootingExtraChance;
    public boolean blockSpawnerSpawns;
    public int itemSlotChance;
    public int maxUpgradeLevel;
    public List<String> blacklistedWorlds = new ArrayList<>();

    public List<String> permissions = new ArrayList<>();

    public List<PropertyRarity> propertyRarities = new ArrayList<>();
    public List<ItemRarity> itemRarities = new ArrayList<>();
    public int totalPropertyWeight;
    public int totalItemWeight;

    public Material sindrisFavourMaterial;
    public String sindrisFavourName;
    public List<String> sindrisFavourLore = new ArrayList<>();
    public String sindrisFavourProperty;

    public Material socketMaterial;
    public String socketName;
    public List<String> socketLore = new ArrayList<>();
    public List<String> socketColors = new ArrayList<>();

    public Material identifierMaterial;
    public String identifierName;
    public String identifierAuthor;
    public List<String> identifierLore;

    public Material specialCraftingItemMaterial;
    public String specialCraftingItemName;
    public List<String> specialCraftingItemLore;

    public List<String> armorMaterials;
    public List<String> weaponMaterials;

    public String socketTitle;
    public String socketSlot;
    public String unidentifiedLore;

    public List<String> prefixes;
    public List<String> suffixes;

    public List<Enchantment> enchantments = new ArrayList<>();
    public List<PotionEffectType> potionEffectTypes = new ArrayList<>();

    public Config(FileConfiguration config) {
        this.config = config;

        specialMobChance = config.getInt("ConfigValues.specialMobChance");
        sockettedItemChance = config.getInt("ConfigValues.sockettedItemChance");
        unidentifiedChance = config.getInt("ConfigValues.unidentifiedChance");
        maxSockets = config.getInt("ConfigValues.maxSockets");
        socketChance = config.getInt("ConfigValues.socketChance");
        tomeChance = config.getInt("ConfigValues.tomeChance");
        lootingExtraChance = config.getDouble("ConfigValues.lootingExtraChance");
        blockSpawnerSpawns = config.getBoolean("ConfigValues.blockSpawnerSpawns");
        itemSlotChance = config.getInt("ConfigValues.itemSlotChance");
        maxUpgradeLevel = config.getInt("ConfigValues.maxUpgradeLevel");
        blacklistedWorlds = config.getStringList("ConfigValues.blacklistedWorlds");

        loadConfigVariables();

        sindrisFavourMaterial = Material.valueOf(config.getString("SindrisFavour.material"));
        sindrisFavourName = config.getString("SindrisFavour.name").replace("&", "§");
        sindrisFavourLore = config.getStringList("SindrisFavour.lore");
        sindrisFavourProperty = config.getString("SindrisFavour.property").replace("&", "§");

        socketMaterial = Material.valueOf(config.getString("Socket.material"));
        socketName = config.getString("Socket.name").replace("&", "§");
        socketLore = config.getStringList("Socket.lore");
        socketColors = config.getStringList("Socket.colors");

        identifierMaterial = Material.valueOf(config.getString("IdentifierTome.material"));
        identifierName = config.getString("IdentifierTome.name").replace("&", "§");
        identifierAuthor = config.getString("IdentifierTome.author");
        identifierLore = config.getStringList("IdentifierTome.lore");

        specialCraftingItemMaterial = Material.valueOf(config.getString("SpecialCraftingItem.material"));
        specialCraftingItemName = config.getString("SpecialCraftingItem.name").replace("&", "§");
        specialCraftingItemLore = config.getStringList("SpecialCraftingItem.lore");

        armorMaterials = config.getStringList("RareItems.materials.armorMaterial");
        weaponMaterials = config.getStringList("RareItems.materials.weaponMaterial");

        socketTitle = config.getString("RareItems.socketted.socketTitle").replace("&", "§");
        socketSlot = config.getString("RareItems.socketted.socketSlot").replace("&", "§");
        unidentifiedLore = config.getString("RareItems.unidentified.lore").replace("&", "§");

        prefixes = RareProperties.prefixesConfig.getStringList("Prefixes");
        suffixes = RareProperties.suffixesConfig.getStringList("Suffixes");

        Registry.ENCHANTMENT.iterator().forEachRemaining(enchantments::add);
        Registry.EFFECT.iterator().forEachRemaining(potionEffectTypes::add);
    }

    private void loadConfigVariables() {
        permissions = config.getStringList("Permissions");
        loadPropertyRarites();
        loadItemRarites();
    }

    private void loadPropertyRarites() {
        Set<String> configRarities = config.getConfigurationSection("Rarities.Properties").getKeys(false);
        for(String rarity : configRarities) {
            int weight = config.getInt("Rarities.Properties." + rarity + ".weight");
            if(weight <= 0) {
                continue;
            }

            totalPropertyWeight += weight;
            String name = config.getString("Rarities.Properties." + rarity + ".name");
            String color = config.getString("Rarities.Properties." + rarity + ".color");
            List<String> properties = config.getStringList("Rarities.Properties." + rarity + ".properties");
            PropertyRarity propertyRarity = new PropertyRarity(name, color, weight, properties);
            propertyRarities.add(propertyRarity);
        }
    }

    private void loadItemRarites() {
        Set<String> configRarities = config.getConfigurationSection("Rarities.Items").getKeys(false);
        for(String rarity : configRarities) {
            int weight = config.getInt("Rarities.Items." + rarity + ".weight");
            totalItemWeight += weight;
            int maxEnchants = config.getInt("Rarities.Items." + rarity + ".maxEnchants");
            int minEnchants = config.getInt("Rarities.Items." + rarity + ".minEnchants");
            int maxEnchantLevel = config.getInt("Rarities.Items." + rarity + ".maxEnchantLevel");
            int maxProperties = config.getInt("Rarities.Items." + rarity + ".maxProperties");
            String color = config.getString("Rarities.Items." + rarity + ".color");
            ItemRarity itemRarity = new ItemRarity(rarity, maxEnchants, minEnchants, maxEnchantLevel, maxProperties, color, weight);
            itemRarities.add(itemRarity);
        }
    }

    public ItemRarity getRarityFromName(String rarity) {
        int weight = config.getInt("Rarities.Items." + rarity + ".weight");
        int maxEnchants = config.getInt("Rarities.Items." + rarity + ".maxEnchants");
        int minEnchants = config.getInt("Rarities.Items." + rarity + ".minEnchants");
        int maxEnchantLevel = config.getInt("Rarities.Items." + rarity + ".maxEnchantLevel");
        int maxProperties = config.getInt("Rarities.Items." + rarity + ".maxProperties");
        String color = config.getString("Rarities.Items." + rarity + ".color");
        ItemRarity itemRarity = new ItemRarity(rarity, maxEnchants, minEnchants, maxEnchantLevel, maxProperties, color, weight);
        return itemRarity;
    }
}
