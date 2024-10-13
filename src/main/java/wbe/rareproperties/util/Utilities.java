package wbe.rareproperties.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.items.IdentifierTome;
import wbe.rareproperties.items.SindrisFavour;
import wbe.rareproperties.items.Socket;
import wbe.rareproperties.properties.RareProperty;
import wbe.rareproperties.rarities.ItemRarity;
import wbe.rareproperties.rarities.PropertyRarity;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utilities {

    private RareProperties plugin;

    public Utilities(RareProperties plugin) {
        this.plugin = plugin;
    }

    public ArrayList<String> getValid() {
        ArrayList<String> validProperties = new ArrayList<>();
        for(RareProperty property : plugin.getProperties()) {
            validProperties.add(property.getExternalName());
        }

        return validProperties;
    }

    public boolean addProperty(ItemStack item, String property, String level, String color, Player p) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore;

        if(!meta.hasLore()) {
            lore = new ArrayList<>();
        } else {
            lore = meta.getLore();
        }

        if(hasProperty(item, property)) {
            int propertyLevel = RomanToDecimal.romanToDecimal(level);
            String propertyLine = ("&" + color + property + " " + level).replace("&", "§");
            NamespacedKey key = new NamespacedKey(plugin, Normalizer.normalize("RareProperties" + property,
                    Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, propertyLevel);

            int index = 0;
            for(String line : lore) {
                if(line.contains(property)) {
                    break;
                }
                index++;
            }

            lore.set(index, propertyLine);
            meta.setLore(lore);
            item.setItemMeta(meta);
            p.sendMessage(RareProperties.messages.propertyAdded.replace("%property%", property));
            return true;
        }

        NamespacedKey limitKey = new NamespacedKey(plugin, "RarePropertiesLimit");
        if(!meta.getPersistentDataContainer().has(limitKey)) {
            meta.getPersistentDataContainer().set(limitKey, PersistentDataType.INTEGER, 1);
        } else {
            int limit = meta.getPersistentDataContainer().get(limitKey, PersistentDataType.INTEGER);
            String permission = checkPermissions(p);
            if(permission != null) {
                int permissionLimit = Integer.parseInt(permission.split("\\.")[2]);
                if(limit >= permissionLimit) {
                    p.sendMessage(RareProperties.messages.limited
                            .replace("%limit%", String.valueOf(limit))
                            .replace("%permission%", String.valueOf(permissionLimit)));
                    return false;
                }
            } else {
                p.sendMessage(RareProperties.messages.limited
                        .replace("%limit%", String.valueOf(limit))
                        .replace("%permission%", "0"));
                return false;
            }
            meta.getPersistentDataContainer().set(limitKey, PersistentDataType.INTEGER, limit + 1);
        }

        String propertyLine = ("&" + color + property + " " + level).replace("&", "§");
        int propertyLevel = RomanToDecimal.romanToDecimal(level);
        lore.add(propertyLine);
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, Normalizer.normalize("RareProperties" + property,
                Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, propertyLevel);

        item.setItemMeta(meta);
        p.sendMessage(RareProperties.messages.propertyAdded.replace("%property%", property));
        return true;
    }

    public void addProperty(ItemStack item, String property, int level, String color, boolean noLimit) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore;

        if(!meta.hasLore()) {
            lore = new ArrayList<>();
        } else {
            lore = meta.getLore();
        }

        if(hasProperty(item, property)) {
            String propertyLine = (color + property + " " + DecimalToRoman.intToRoman(level)).replace("&", "§");
            NamespacedKey key = new NamespacedKey(plugin, Normalizer.normalize("RareProperties" + property,
                    Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, level);

            NamespacedKey limitKey = new NamespacedKey(plugin, "RarePropertiesLimit");
            int limit = meta.getPersistentDataContainer().get(limitKey, PersistentDataType.INTEGER) - 1;
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, limit);

            int index = 0;
            for(String line : lore) {
                if(line.contains(property)) {
                    break;
                }
                index++;
            }

            lore.set(index, propertyLine);
            meta.setLore(lore);
            item.setItemMeta(meta);
            return;
        }

        String romanLevel = DecimalToRoman.intToRoman(level);
        String propertyLine = (color + property + " " + romanLevel).replace("&", "§");
        lore.add(propertyLine);
        meta.setLore(lore);

        if(!noLimit) {
            NamespacedKey limitKey = new NamespacedKey(plugin, "RarePropertiesLimit");
            if(!meta.getPersistentDataContainer().has(limitKey)) {
                meta.getPersistentDataContainer().set(limitKey, PersistentDataType.INTEGER, 1);
            } else {
                int limit = meta.getPersistentDataContainer().get(limitKey, PersistentDataType.INTEGER) + 1;
                meta.getPersistentDataContainer().set(limitKey, PersistentDataType.INTEGER, limit);
            }
        }

        NamespacedKey key = new NamespacedKey(plugin, Normalizer.normalize("RareProperties" + property,
                Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, level);

        item.setItemMeta(meta);
    }

    public void removeProperty(ItemStack item, String property, Player p) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, Normalizer.normalize("RareProperties" + property, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));

        if(!hasProperty(item, property)) {
            p.sendMessage(RareProperties.messages.propertyNotPresent);
            return;
        }

        int level = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);

        if(meta.hasLore()) {
            List<String> lore = meta.getLore();
            int line = getLine(lore, property);

            if(line == -1) {
                p.sendMessage(RareProperties.messages.propertyNotPresent);
                return;
            }

            lore.remove(line);
            meta.getPersistentDataContainer().remove(key);

            meta.setLore(lore);
        }

        NamespacedKey limitKey = new NamespacedKey(plugin, "RarePropertiesLimit");
        if(meta.getPersistentDataContainer().has(limitKey)) {
            int limit = meta.getPersistentDataContainer().get(limitKey, PersistentDataType.INTEGER) - 1;
            if(limit < 1) {
                meta.getPersistentDataContainer().remove(limitKey);
            } else {
                meta.getPersistentDataContainer().set(limitKey, PersistentDataType.INTEGER, limit);
            }
        }

        item.setItemMeta(meta);

        if(getProperty(property) != null) {
            giveProperty(property, DecimalToRoman.intToRoman(level), p);
        }
        p.sendMessage(RareProperties.messages.propertyRemoved.replace("%property%", property));
    }

    public void giveProperty(String property, String level, Player p) {
        SindrisFavour sindrisFavour = new SindrisFavour();
        sindrisFavour.setProperty(getProperty(property), level, plugin);
        if(p.getInventory().firstEmpty() == -1) {
            p.getWorld().dropItem(p.getLocation(), sindrisFavour);
        } else {
            p.getInventory().addItem(sindrisFavour);
        }
        p.sendMessage(RareProperties.messages.propertyGiven.replace("%property%", property).replace("%level%", level));
        p.updateInventory();
    }

    public void giveRandomProperty(String property, Player p) {
        Random random = new Random();
        int lvl = random.nextInt(5) + 1;
        String level = DecimalToRoman.intToRoman(lvl);

        SindrisFavour sindrisFavour = new SindrisFavour();
        sindrisFavour.setProperty(getProperty(property), level, plugin);
        if(p.getInventory().firstEmpty() == -1) {
            p.getWorld().dropItem(p.getLocation(), sindrisFavour);
        } else {
            p.getInventory().addItem(sindrisFavour);
        }
        p.updateInventory();
    }

    public void giveSocket(Player player, String color) {
        Socket socket = new Socket(plugin);
        if(color != null) {
            socket = new Socket(plugin, color.toUpperCase());
        }

        if(player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), socket);
        } else {
            player.getInventory().addItem(socket);
        }
        player.sendMessage(RareProperties.messages.socketGiven);
        player.updateInventory();
    }

    public void giveTome(Player player) {
        IdentifierTome tome = new IdentifierTome(plugin);
        if(player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), tome);
        } else {
            player.getInventory().addItem(tome);
        }
        player.sendMessage(RareProperties.messages.tomeGiven);
        player.updateInventory();
    }

    public boolean hasProperty(ItemStack item, String property) {
        ItemMeta meta = item.getItemMeta();

        NamespacedKey key = new NamespacedKey(plugin, Normalizer.normalize("RareProperties" + property, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
        if(meta == null) {
            return false;
        }

        PersistentDataContainer itemData = meta.getPersistentDataContainer();
        if(itemData.has(key, PersistentDataType.INTEGER)) {
            return true;
        }

        return false;
    }

    public int getLine(List<String> lore, String property) {
        int size = lore.size();

        for (int i = 0; i < size; i++) {
            String loreLine = lore.get(i).toString();
            if (loreLine.contains(property)) {
                return i;
            }
        }

        return -1;
    }

    public RareProperty getProperty(String property) {
        for(RareProperty rareProperty : plugin.getProperties()) {
            if(rareProperty.getExternalName().equalsIgnoreCase(property)) {
                return rareProperty;
            }
        }
        return null;
    }

    public ItemRarity calculateItemRarity() {
        Random random = new Random();
        int randomNumber = random.nextInt(RareProperties.config.totalItemWeight);
        int weight = 0;
        List<ItemRarity> rarities = RareProperties.config.itemRarities;

        for(ItemRarity rarity : rarities) {
            weight += rarity.getWeight();
            if(randomNumber < weight) {
                return rarity;
            }
        }

        return rarities.get(rarities.size() - 1);
    }

    public PropertyRarity calculatePropertyRarity() {
        Random random = new Random();
        int randomNumber = random.nextInt(RareProperties.config.totalPropertyWeight);
        int weight = 0;
        List<PropertyRarity> rarities = RareProperties.config.propertyRarities;

        for(PropertyRarity rarity : rarities) {
            weight += rarity.getWeight();
            if(randomNumber < weight) {
                return rarity;
            }
        }

        return rarities.get(rarities.size() - 1);
    }

    public String getRandomPrefix() {
        Random random = new Random();
        String prefix = RareProperties.config.prefixes.get(random.nextInt(RareProperties.config.prefixes.size()));
        return prefix;
    }

    public String getRandomSuffix() {
        Random random = new Random();
        String suffix = RareProperties.config.suffixes.get(random.nextInt(RareProperties.config.suffixes.size()));
        return suffix;
    }

    public Enchantment getRandomEnchantment() {
        Random random = new Random();
        Enchantment enchantment = RareProperties.config.enchantments.get(
                random.nextInt(RareProperties.config.enchantments.size()));
        return enchantment;
    }

    public String checkPermissions(Player player) {
        for(String permission : RareProperties.config.permissions) {
            if(player.hasPermission(permission)) {
                return permission;
            }
        }
        return null;
    }

    public ItemStack addSocket(ItemStack item, String color) {
        NamespacedKey colorsKey = new NamespacedKey(plugin, "rarepropertiessocketslots");
        NamespacedKey limitKey = new NamespacedKey(plugin, "RarePropertiesLimit");

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if(lore == null) {
            lore = new ArrayList<>();
        }

        if(meta.getPersistentDataContainer().has(colorsKey)) {
            int index = findLine(item, RareProperties.config.socketSlot);
            lore.set(index, lore.get(index) + ChatColor.valueOf(color) + RareProperties.config.socketSlot + " ");
            meta.setLore(lore);
            String colors = meta.getPersistentDataContainer().get(colorsKey, PersistentDataType.STRING);
            meta.getPersistentDataContainer().set(colorsKey, PersistentDataType.STRING, colors + "." + color);
        } else {
            lore.add("");
            lore.add(RareProperties.config.socketTitle);
            lore.add(ChatColor.valueOf(color) + RareProperties.config.socketSlot + " ");
            meta.setLore(lore);
            meta.getPersistentDataContainer().set(colorsKey, PersistentDataType.STRING, color);
        }

        if(meta.getPersistentDataContainer().has(limitKey)) {
            int limit = meta.getPersistentDataContainer().get(limitKey, PersistentDataType.INTEGER);
            meta.getPersistentDataContainer().set(limitKey, PersistentDataType.INTEGER, limit + 1);
        } else {
            meta.getPersistentDataContainer().set(limitKey, PersistentDataType.INTEGER, 1);
        }

        item.setItemMeta(meta);
        return item;
    }

    public boolean applySocket(ItemStack item, String color, String colors) {
        // Sacamos la línea que tiene los sockets
        int index = findLine(item, RareProperties.config.socketSlot);
        List<String> lore = item.getItemMeta().getLore();
        String[] sockets = colors.split("\\.");

        // Sacamos el socket que coincide con el color
        int socketSlot = findColorSocket(sockets, color);
        if(socketSlot == -1) {
            return false;
        }

        // Comprobamos si quedan más sockets
        NamespacedKey colorsKey = new NamespacedKey(plugin, "rarepropertiessocketslots");
        ItemMeta meta = item.getItemMeta();
        if(sockets.length - 1 == 0) {
            // Si no quedan eliminados las líneas de los sockets.
            for(int i=index;i>0;i--) {
                lore.remove(i);
            }
            meta.getPersistentDataContainer().remove(colorsKey);
        } else {
            // Si quedan, eliminamos el socket que coincide
            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder keyString =  new StringBuilder();
            int size = sockets.length;
            for(int i=0;i<size;i++) {
                if(i != socketSlot) {
                    stringBuilder.append(ChatColor.valueOf(sockets[i]) + RareProperties.config.socketSlot + " ");
                    keyString.append(sockets[i] + ".");
                }
            }
            lore.set(index, stringBuilder.toString());
            meta.getPersistentDataContainer().set(colorsKey, PersistentDataType.STRING,
                    keyString.toString().substring(0, keyString.toString().length() - 1));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        // Aplicamos la propiedad
        PropertyRarity rarity = calculatePropertyRarity();
        Random random = new Random();
        String property = rarity.getProperties().get(
                random.nextInt(rarity.getProperties().size()));
        addProperty(item, property, random.nextInt(5) + 1, rarity.getColor(), true);
        return true;
    }

    private int findColorSocket(String[] colors, String color) {
        int size = colors.length;
        for(int i=0;i<size;i++) {
            if(colors[i].equalsIgnoreCase(color)) {
                return i;
            }
        }

        return -1;
    }

    private int findLine(ItemStack item, String line) {
        List<String> lore = item.getItemMeta().getLore();
        int size = lore.size();
        for(int i=0;i<size;i++) {
            if(lore.get(i).contains(line)) {
                return i;
            }
        }

        return -1;
    }
}