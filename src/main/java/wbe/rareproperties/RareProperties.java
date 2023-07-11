package wbe.rareproperties;

import java.io.Console;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.sun.tools.javac.jvm.Items;
import jdk.vm.ci.meta.MethodHandleAccessProvider;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class RareProperties extends JavaPlugin implements Listener {

    static ItemStack objeto = null;

    List<Player> cobrarComida = new ArrayList<>();

    Runnable bkr = null;

    ArrayList<String> tipos = new ArrayList<String>(Arrays.asList("Vuelo", "Reparación", "Andanada", "Captura"));

    ArrayList<EntityType> mobs = new ArrayList<EntityType>(Arrays.asList(EntityType.ALLAY, EntityType.AXOLOTL, EntityType.BAT,
            EntityType.BEE, EntityType.BLAZE, EntityType.CAT, EntityType.CAVE_SPIDER, EntityType.CHICKEN, EntityType.COD,
            EntityType.COW, EntityType.CREEPER, EntityType.DOLPHIN, EntityType.DONKEY, EntityType.DROWNED, EntityType.ELDER_GUARDIAN,
            EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.EVOKER, EntityType.FOX, EntityType.FROG, EntityType.GHAST,
            EntityType.GLOW_SQUID, EntityType.GOAT, EntityType.GUARDIAN, EntityType.HOGLIN, EntityType.HORSE, EntityType.HUSK,
            EntityType.IRON_GOLEM, EntityType.LLAMA, EntityType.MAGMA_CUBE, EntityType.MUSHROOM_COW, EntityType.MULE, EntityType.OCELOT,
            EntityType.PANDA, EntityType.PARROT, EntityType.PHANTOM, EntityType.PIG, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE,
            EntityType.PILLAGER, EntityType.POLAR_BEAR, EntityType.PUFFERFISH, EntityType.RABBIT, EntityType.RAVAGER,
            EntityType.SALMON, EntityType.SHEEP, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SKELETON_HORSE,
            EntityType.SLIME, EntityType.SNOWMAN, EntityType.SPIDER, EntityType.SQUID, EntityType.STRAY, EntityType.STRIDER, EntityType.TADPOLE,
            EntityType.TRADER_LLAMA, EntityType.TROPICAL_FISH, EntityType.TURTLE, EntityType.VEX, EntityType.VILLAGER, EntityType.VINDICATOR,
            EntityType.WANDERING_TRADER, EntityType.WARDEN, EntityType.WITCH, EntityType.WITHER_SKELETON, EntityType.WOLF, EntityType.ZOGLIN,
            EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN));
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)this);
    }

    public void onDisable() {
        reloadConfig();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("RareProperties")) {
            Player p = null;
            if(sender instanceof Player) {
                p = (Player) sender;
            }
            if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
                if(!p.hasPermission("rareproperties.command.help")) {
                    p.sendMessage(this.getConfig().getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                for(String x : this.getConfig().getStringList("Messages.help")) {
                    p.sendMessage(x.replace("&", "§"));
                }
            } else if(args[0].equalsIgnoreCase("list")) {
                if(!p.hasPermission("rareproperties.command.list")) {
                    p.sendMessage(this.getConfig().getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                p.sendMessage(this.getConfig().getString("Messages.listMessage").replace("&", "§") + "\n" + String.valueOf(tipos));
            } else if(args[0].equalsIgnoreCase("add")) {
                if (!p.hasPermission("rareproperties.command.add")) {
                    p.sendMessage(this.getConfig().getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                if (args.length != 4) {
                    p.sendMessage(this.getConfig().getString("Messages.notEnoughArgs").replace("&", "§"));
                    p.sendMessage(this.getConfig().getString("Messages.addParams").replace("&", "§"));
                    return false;
                }
                ItemStack item = p.getInventory().getItemInMainHand();
                addProperty(item, args[1], args[2], args[3], p, false);
                p.updateInventory();
            } else if(args[0].equalsIgnoreCase("addDiablo")) {
                if(!p.hasPermission("rareproperties.command.add")) {
                    p.sendMessage(this.getConfig().getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                if(args.length != 4) {
                    p.sendMessage(this.getConfig().getString("Messages.notEnoughArgs").replace("&", "§"));
                    p.sendMessage(this.getConfig().getString("Messages.addDiabloParams").replace("&", "§"));
                    return false;
                }
                ItemStack item = p.getInventory().getItemInMainHand();
                addProperty(item, args[1], args[2], args[3], p, true);
                p.updateInventory();
            } else if(args[0].equalsIgnoreCase("remove")) {
                if(!p.hasPermission("rareproperties.command.remove")) {
                    p.sendMessage(this.getConfig().getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                if(args.length != 2) {
                    p.sendMessage(this.getConfig().getString("Messages.notEnoughArgs").replace("&", "§"));
                    p.sendMessage(this.getConfig().getString("Messages.removeParams").replace("&", "§"));
                    return false;
                }
                ItemStack item = p.getInventory().getItemInMainHand();
                removeProperty(item, args[1], p);
                p.updateInventory();
            } else if(args[0].equalsIgnoreCase("get")) {
                if (!p.hasPermission("rareproperties.command.get")) {
                    p.sendMessage(this.getConfig().getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                if (args.length != 3) {
                    p.sendMessage(this.getConfig().getString("Messages.notEnoughArgs").replace("&", "§"));
                    p.sendMessage(this.getConfig().getString("Messages.getParams").replace("&", "§"));
                    return false;
                }
                ItemStack item = p.getInventory().getItemInMainHand();
                giveProperty(args[1], args[2], p, false);
            } else if(args[0].equalsIgnoreCase("getDiablo")) {
                if (!p.hasPermission("rareproperties.command.get")) {
                    p.sendMessage(this.getConfig().getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                if (args.length != 3) {
                    p.sendMessage(this.getConfig().getString("Messages.notEnoughArgs").replace("&", "§"));
                    p.sendMessage(this.getConfig().getString("Messages.getDiabloParams").replace("&", "§"));
                    return false;
                }
                ItemStack item = p.getInventory().getItemInMainHand();
                giveProperty(args[1], args[2], p, true);
            } else if(args[0].equalsIgnoreCase("give")) {
                if(!p.hasPermission("rareproperties.command.give")) {
                    p.sendMessage(this.getConfig().getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                if(args.length != 4) {
                    p.sendMessage(this.getConfig().getString("Messages.notEnoughArgs").replace("&", "§"));
                    p.sendMessage(this.getConfig().getString("Messages.giveParams").replace("&", "§"));
                    return false;
                }
                Player otherPlayer = Bukkit.getServer().getPlayer(args[1]);
                giveProperty(args[2], args[3], otherPlayer, false);
            } else if(args[0].equalsIgnoreCase("giveDiablo")) {
                if(!p.hasPermission("rareproperties.command.give")) {
                    p.sendMessage(this.getConfig().getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                if(args.length != 4) {
                    p.sendMessage(this.getConfig().getString("Messages.notEnoughArgs").replace("&", "§"));
                    p.sendMessage(this.getConfig().getString("Messages.giveDiabloParams").replace("&", "§"));
                    return false;
                }
                Player otherPlayer = Bukkit.getServer().getPlayer(args[1]);
                giveProperty(args[2], args[3], otherPlayer, true);
            } else if(args[0].equalsIgnoreCase("giveRandom")) {
                if(!sender.hasPermission("rareproperties.command.giveRandom")) {
                    sender.sendMessage(this.getConfig().getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                if(args.length != 3) {
                    sender.sendMessage(this.getConfig().getString("Messages.notEnoughArgs").replace("&", "§"));
                    sender.sendMessage(this.getConfig().getString("Messages.giveRandomParams").replace("&", "§"));
                    return false;
                }
                Player otherPlayer = Bukkit.getServer().getPlayer(args[1]);
                giveRandomProperty(args[2], otherPlayer);
            } else if(args[0].equalsIgnoreCase("giveRandomDiablo")) {
                if(!sender.hasPermission("rareproperties.command.giveRandom")) {
                    sender.sendMessage(this.getConfig().getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                if(args.length != 4) {
                    sender.sendMessage(this.getConfig().getString("Messages.notEnoughArgs").replace("&", "§"));
                    sender.sendMessage(this.getConfig().getString("Messages.giveRandomDiabloParams").replace("&", "§"));
                    return false;
                }
                Player otherPlayer = Bukkit.getServer().getPlayer(args[1]);
                giveRandomDiabloProperty(args[2], args[3], otherPlayer);
            }
        }
        return true;
    }

    private void addProperty(ItemStack item, String property, String level, String color, Player p, boolean diablo) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore;

        if(!meta.hasLore()) {
            lore = new ArrayList<String>();
        } else {
            lore = meta.getLore();
        }

        if(hasProperty(item, property)) {
            p.sendMessage(this.getConfig().getString("Messages.alreadyHasProperty").replace("&", "§"));
            return;
        }

        String propertyLine;
        if(diablo) {
            propertyLine = ("&" + color + level + " " + property).replace("&", "§");
        } else {
            propertyLine = ("&" + color + property + " " + level).replace("&", "§");
        }
        lore.add(propertyLine);

        p.sendMessage(this.getConfig().getString("Messages.propertyAdded").replace("&", "§").replace("%property%", property));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    private void removeProperty(ItemStack item, String property, Player p) {
        ItemMeta meta = item.getItemMeta();
        if(!meta.hasLore()) {
            p.sendMessage(this.getConfig().getString("Messages.propertyNotPresent").replace("&", "§"));
            return;
        }
        List<String> lore = meta.getLore();
        int len = lore.size();
        int line = -1;

        for (int i = 0; i < len; i++) {
            String loreLine = lore.get(i).toString();
            if (loreLine.contains(property)) {
                line = i;
                break;
            }
        }

        if(line == -1) {
            p.sendMessage(this.getConfig().getString("Messages.propertyNotPresent").replace("&", "§"));
            return;
        }

        lore.remove(line);

        p.sendMessage(this.getConfig().getString("Messages.propertyRemoved").replace("&", "§").replace("%property%", property));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    private void giveProperty(String property, String level, Player p, boolean diablo) {
        SindrisFavour sindrisFavour = new SindrisFavour(Material.valueOf(this.getConfig().getString("Messages.upgradeMaterial")));
        sindrisFavour.setProperty(property, level, diablo);
        p.getInventory().addItem(new ItemStack[] { sindrisFavour });
        p.sendMessage(this.getConfig().getString("Messages.propertyGiven").replace("&", "§").replace("%property%", property).replace("%level%", level));
        p.updateInventory();
    }

    private void giveRandomProperty(String property, Player p) {
        Random random = new Random();
        int lvl = random.nextInt(5) + 1;
        String level = decimalToRoman(lvl);

        SindrisFavour sindrisFavour = new SindrisFavour(Material.valueOf(this.getConfig().getString("Messages.upgradeMaterial")));
        sindrisFavour.setProperty(property, level, false);
        p.getInventory().addItem(new ItemStack[] { sindrisFavour });
        p.updateInventory();
    }

    private void giveRandomDiabloProperty(String property, String max, Player p) {
        int maximum = Integer.valueOf(max);

        Random random = new Random();
        int lvl = random.nextInt(maximum) + 1;

        String level;
        level = "+" + String.valueOf(lvl);

        SindrisFavour sindrisFavour = new SindrisFavour(Material.valueOf(this.getConfig().getString("Messages.upgradeMaterial")));
        sindrisFavour.setProperty(property, level, true);
        p.getInventory().addItem(new ItemStack[] { sindrisFavour });
        p.updateInventory();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void addPropertyToItem(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getAction() != InventoryAction.SWAP_WITH_CURSOR) {
            return;
        }

        String itemMaterial = this.getConfig().getString("Messages.upgradeMaterial");
        Material playerItemMaterial = e.getCursor().getType();
        if(!playerItemMaterial.equals(Material.matchMaterial(itemMaterial))) {
            return;
        }

        ItemStack playerItem = e.getCursor();
        ItemMeta meta = playerItem.getItemMeta();
        if(!meta.getDisplayName().contains("Favor de Sindri")) {
            return;
        }

        if(!meta.hasLore()) {
            return;
        }

        List<String> lore = meta.getLore();
        ArrayList<String> trueLore = new ArrayList<String>(Arrays.asList("","Sindri te hará el favor de mejorar","tu objeto con la siguiente propiedad:"));
        String propertyInfo = "";
        int len = lore.size();
        for(int i=0;i<len;i++) {
            String line = lore.get(i);
            if(i == 3) {
                propertyInfo = line;
                break;
            }

            if(!line.contains(trueLore.get(i))) {
                return;
            }
        }

        //Now I try to add the property
        ItemStack inventoryItem = e.getCurrentItem();
        ItemMeta inventoryItemMeta = inventoryItem.getItemMeta();
        List<String> inventoryLore;
        if(!inventoryItemMeta.hasLore()) {
            inventoryLore = new ArrayList<String>();
        } else {
            inventoryLore = inventoryItemMeta.getLore();
        }

        String name = "";
        if(!inventoryItemMeta.hasDisplayName()) {
            name = inventoryItemMeta.getLocalizedName();
        } else {
            name = inventoryItemMeta.getDisplayName();
        }

        String newLine = "§d" + propertyInfo;
        inventoryLore.add(newLine);
        p.sendMessage(this.getConfig().getString("Messages.propertyAddedDisc").replace("&", "§"));
        inventoryItemMeta.setLore(inventoryLore);
        inventoryItemMeta.setDisplayName(name);
        inventoryItem.setItemMeta(inventoryItemMeta);
        p.setItemOnCursor(null);
        e.setCancelled(true);
        p.updateInventory();
    }

    public boolean hasProperty(ItemStack item, String property) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<String>();

        if(meta.hasLore()) {
            lore = meta.getLore();
            int len = lore.size();
            for (int i = 0; i < len; i++) {
                String loreLine = lore.get(i).toString();
                if (loreLine.contains(property)) {
                    return true;
                }
            }
        }

        return false;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerEquip(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            boolean fly = false;
            int level = 0;
            //Fly
            if (player.hasPermission("rareproperties.use.fly")) {
                PlayerInventory in = player.getInventory();
                ItemStack[] arrayOfItemStack;
                if ((arrayOfItemStack = in.getArmorContents()).length != 0) {
                    ItemStack is = arrayOfItemStack[0];
                    level = comprobacionObjetoFly(is);
                    if(level > 0)
                        fly = true;
                    if (fly && player.getFoodLevel() <= 1) {
                        player.setAllowFlight(false);
                        player.setFoodLevel(1);
                    }
                }
                if (!fly) {
                    ItemStack hand = in.getItemInMainHand();
                    level = comprobacionObjetoFly(hand);
                    if(level > 0)
                        fly = true;
                }
                player.setAllowFlight(fly);
                if (!fly) {
                    player.setFlySpeed(0.1F);
                } else {
                    player.setFlySpeed(0.1F);
                }
                if (this.cobrarComida.contains(player)) {
                    if (this.bkr == null) {
                        if (objeto != null) {
                            int rest = (int)(5.0F / level * 10.0F);
                            final int food = rest;
                            this.bkr = new Runnable() {
                                public void run() {
                                    RareProperties.this.cobrarComida.remove(player);
                                    if (!player.isOnGround())
                                        player.setFoodLevel(player.getFoodLevel() - food);
                                    RareProperties.this.bkr = null;
                                }
                            };
                        }
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this, this.bkr, 120L);
                    }
                } else if (fly && (player.isFlying() || player.isGliding())) {
                    this.cobrarComida.add(player);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteractRareItem(PlayerInteractEvent event) {
        boolean repair = false;
        boolean burst = false;
        int level = 0;

        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }

        Player player = event.getPlayer();

        if(player.getInventory().getItemInMainHand() == null) {
            return;
        }

        if(hasProperty(player.getInventory().getItemInMainHand(), "Reparación")) {
            if (!player.hasPermission("rareproperties.use.repair")) {
                return;
            } else {
                PlayerInventory in = player.getInventory();
                if (!repair) {
                    ItemStack hand = in.getItemInMainHand();
                    level = comprobacionObjetoRepair(hand);
                    if (level > 0)
                        repair = true;
                }

                if (!repair) {
                    return;
                }

                int coste = 10 - level;
                ItemStack primerObjeto = in.getItem(0);
                Damageable meta = (Damageable) primerObjeto.getItemMeta();
                if (player.getFoodLevel() > coste && primerObjeto != null && meta.getDamage() > 0) {
                    int cantidad = Math.round(primerObjeto.getType().getMaxDurability() / 100 * 10 * level);
                    int durabilidad = (meta.getDamage() - cantidad);

                    if (durabilidad < 0) {
                        durabilidad = 0;
                    }

                    player.setFoodLevel(player.getFoodLevel() - coste);
                    meta.setDamage(durabilidad);
                    primerObjeto.setItemMeta(meta);

                    //in.setItem(0, primerObjeto);
                    player.updateInventory();
                }
            }
        } else if(hasProperty(player.getInventory().getItemInMainHand(), "Andanada")) {
            if(!player.hasPermission("rareproperties.use.burst")) {
                return;
            }

            PlayerInventory in = player.getInventory();
            if(player.getInventory().getItemInMainHand() == null) {
                return;
            }

            if (!burst) {
                ItemStack hand = in.getItemInMainHand();
                level = comprobacionObjetoBurst(hand);
                if (level > 0)
                    burst = true;
            }

            if (!burst) {
                return;
            }

            int coste = 11 - level;

            if(player.getFoodLevel() < coste) {
                return;
            }

            List<Entity> nearbyEntities = player.getNearbyEntities(8, 8, 8);
            Vector vPlayer = player.getLocation().toVector();

            for(Entity ent : nearbyEntities) {
                Vector unitVector = ent.getLocation().toVector().subtract(vPlayer).normalize();
                unitVector.setY(0.55/level);
                ent.setVelocity(unitVector.multiply(level*2));
            }

            player.setFoodLevel(player.getFoodLevel() - coste);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMobDeath(EntityDeathEvent event) {
        boolean capture = false;
        try {
            Player p = event.getEntity().getKiller();
            LivingEntity entity = event.getEntity();

            if(p.getInventory().getItemInMainHand() == null) {
                return;
            }

            if (!hasProperty(p.getInventory().getItemInMainHand(), "Captura")) {
                return;
            }

            if (!p.hasPermission("rareproperties.use.capture")) {
                return;
            }

            int level = 0;
            PlayerInventory in = p.getInventory();
            if (!capture) {
                ItemStack cursor = in.getItemInMainHand();
                level = comprobacionObjetoCapture(cursor);
                if (level > 0)
                    capture = true;
            }

            if (!capture) {
                return;
            }

            if (!mobs.contains(entity.getType())) {
                return;
            }

            Random random = new Random();
            int prob = random.nextInt(10000);
            if (level < prob) {
                return;
            }

            String conversion = "";
            if (entity.getType().equals(EntityType.MUSHROOM_COW)) {
                conversion = "MOOSHROOM_SPAWN_EGG";
            } else if (entity.getType().equals(EntityType.SNOWMAN)) {
                conversion = "SNOW_GOLEM_SPAWN_EGG";
            } else {
                conversion = String.valueOf(entity.getType().toString()) + "_SPAWN_EGG";
            }

            Material eggMaterial = Material.valueOf(conversion);

            ItemStack egg = new ItemStack(eggMaterial);
            egg.setAmount(1);
            event.getDrops().add(egg);
        } catch(Exception e) {
            return;
        }
    }

    private int comprobacionObjetoFly(ItemStack is) {
        int level = 0;
        if (is != null && is.hasItemMeta()) {
            List<String> lore = is.getItemMeta().getLore();
            try {
                for (String linea : lore) {
                    if (linea.contains("Vuelo")) {
                        String flyLevel = null;
                        try {
                            flyLevel = linea.split(" ")[1];
                        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
                        if (flyLevel != null)
                            level = (RomanToDecimal.romanToDecimal(flyLevel));
                        objeto = is;
                        return level;
                    }
                }
            } catch (Exception exception) {}
        }
        return level;
    }

    private int comprobacionObjetoRepair(ItemStack is) {
        int level = 0;
        if (is != null && is.hasItemMeta()) {
            List<String> lore = is.getItemMeta().getLore();
            try {
                for (String linea : lore) {
                    if (linea.contains("Reparación")) {
                        String repairLevel = null;
                        try {
                            repairLevel = linea.split(" ")[1];
                        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
                        if (repairLevel != null) {
                            level = (RomanToDecimal.romanToDecimal(repairLevel));
                        }
                        objeto = is;
                        return level;
                    }
                }
            } catch (Exception exception) {}
        }
        return level;
    }

    private int comprobacionObjetoBurst(ItemStack is) {
        int level = 0;
        if (is != null && is.hasItemMeta()) {
            List<String> lore = is.getItemMeta().getLore();
            try {
                for (String linea : lore) {
                    if (linea.contains("Andanada")) {
                        String burstLevel = null;
                        try {
                            burstLevel = linea.split(" ")[1];
                        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
                        if (burstLevel != null) {
                            level = (RomanToDecimal.romanToDecimal(burstLevel));
                        }
                        objeto = is;
                        return level;
                    }
                }
            } catch (Exception exception) {}
        }
        return level;
    }

    private int comprobacionObjetoCapture(ItemStack is) {
        int level = 0;
        if (is != null && is.hasItemMeta()) {
            List<String> lore = is.getItemMeta().getLore();
            try {
                for (String linea : lore) {
                    if (linea.contains("Captura")) {
                        String captureLevel = null;
                        try {
                            captureLevel = linea.split(" ")[1];
                        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
                        if (captureLevel != null) {
                            level = (RomanToDecimal.romanToDecimal(captureLevel));
                        }
                        objeto = is;
                        return level;
                    }
                }
            } catch (Exception exception) {}
        }
        return level;
    }

    private String decimalToRoman(int decimal) {
        switch(decimal) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
        }

        return "";
    }
}

