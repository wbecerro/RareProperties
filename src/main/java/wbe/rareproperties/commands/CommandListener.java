package wbe.rareproperties.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.config.Messages;
import wbe.rareproperties.items.ItemManager;
import wbe.rareproperties.util.Utilities;

public class CommandListener implements CommandExecutor {

    private RareProperties plugin;

    private FileConfiguration config;

    private Utilities utilities;

    public CommandListener(RareProperties plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
        this.utilities = new Utilities(plugin);
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("RareProperties")) {
            Player p = null;
            if(sender instanceof Player) {
                p = (Player) sender;
            }
            if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
                if(!p.hasPermission("rareproperties.command.help")) {
                    p.sendMessage(RareProperties.messages.noPermission);
                    return false;
                }
                for(String line : RareProperties.messages.help) {
                    p.sendMessage(line.replace("&", "§"));
                }
            } else if(args[0].equalsIgnoreCase("list")) {
                if(!p.hasPermission("rareproperties.command.list")) {
                    p.sendMessage(RareProperties.messages.noPermission);
                    return false;
                }
                p.sendMessage(RareProperties.messages.listMessage + "\n" + String.valueOf(utilities.getValid()));
            } else if(args[0].equalsIgnoreCase("add")) {
                if(!p.hasPermission("rareproperties.command.add")) {
                    p.sendMessage(RareProperties.messages.noPermission);
                    return false;
                }
                if(args.length != 4) {
                    p.sendMessage(RareProperties.messages.notEnoughArgs);
                    p.sendMessage(RareProperties.messages.addParams);
                    return false;
                }
                ItemStack item = p.getInventory().getItemInMainHand();
                utilities.addProperty(item, args[1], args[2], args[3], p);
                p.updateInventory();
            } else if(args[0].equalsIgnoreCase("remove")) {
                if(!p.hasPermission("rareproperties.command.remove")) {
                    p.sendMessage(RareProperties.messages.noPermission);
                    return false;
                }
                if(args.length != 2) {
                    p.sendMessage(RareProperties.messages.notEnoughArgs);
                    p.sendMessage(RareProperties.messages.removeParams);
                    return false;
                }
                ItemStack item = p.getInventory().getItemInMainHand();
                utilities.removeProperty(item, args[1], p);
                p.updateInventory();
            } else if(args[0].equalsIgnoreCase("get")) {
                if(!p.hasPermission("rareproperties.command.get")) {
                    p.sendMessage(RareProperties.messages.noPermission);
                    return false;
                }
                if(args.length != 3) {
                    p.sendMessage(RareProperties.messages.notEnoughArgs);
                    p.sendMessage(RareProperties.messages.getParams);
                    return false;
                }
                utilities.giveProperty(args[1], args[2], p);
            } else if(args[0].equalsIgnoreCase("give")) {
                if(!p.hasPermission("rareproperties.command.give")) {
                    p.sendMessage(RareProperties.messages.noPermission);
                    return false;
                }
                if(args.length != 4) {
                    p.sendMessage(RareProperties.messages.notEnoughArgs);
                    p.sendMessage(RareProperties.messages.giveParams);
                    return false;
                }
                Player otherPlayer = Bukkit.getServer().getPlayer(args[1]);
                utilities.giveProperty(args[2], args[3], otherPlayer);
            } else if(args[0].equalsIgnoreCase("giveRandom")) {
                if(!sender.hasPermission("rareproperties.command.giveRandom")) {
                    sender.sendMessage(RareProperties.messages.noPermission);
                    return false;
                }
                if(args.length != 3) {
                    sender.sendMessage(RareProperties.messages.notEnoughArgs);
                    sender.sendMessage(RareProperties.messages.giveRandomParams);
                    return false;
                }
                Player otherPlayer = Bukkit.getServer().getPlayer(args[1]);
                utilities.giveRandomProperty(args[2], otherPlayer);
            } else if(args[0].equalsIgnoreCase("reload")) {
                if(!sender.hasPermission("rareproperties.command.reload")) {
                    sender.sendMessage(RareProperties.messages.noPermission);
                    return false;
                }
                plugin.saveDefaultConfig();
                plugin.reloadConfig();
                RareProperties.reloadConfiguration(config);
                sender.sendMessage(RareProperties.messages.reload);
            } else if(args[0].equalsIgnoreCase("socket")) {
                if(!sender.hasPermission("rareproperties.command.socket")) {
                    sender.sendMessage(RareProperties.messages.noPermission);
                    return false;
                }

                if(args.length > 1) {
                    utilities.giveSocket(p, args[1]);
                } else {
                    utilities.giveSocket(p, null);
                }
            } else if(args[0].equalsIgnoreCase("tome")) {
                if(!sender.hasPermission("rareproperties.command.tome")) {
                    sender.sendMessage(RareProperties.messages.noPermission);
                    return false;
                }
                utilities.giveTome(p);
            } else if(args[0].equalsIgnoreCase("showItem")) {
                if(!sender.hasPermission("rareproperties.command.showItem")) {
                    sender.sendMessage(RareProperties.messages.noPermission);
                    return false;
                }
                p.sendMessage(p.getInventory().getItemInMainHand().toString());
            } else if(args[0].equalsIgnoreCase("getItemArmor")) {
                if(!sender.hasPermission("rareproperties.command.getItem")) {
                    sender.sendMessage(RareProperties.messages.noPermission);
                    return false;
                }
                p.getInventory().addItem(new ItemStack[]{RareProperties.itemManager.generateRandomItem(true)});
            } else if(args[0].equalsIgnoreCase("getItemWeapon")) {
                if(!sender.hasPermission("rareproperties.command.showItem")) {
                    sender.sendMessage(RareProperties.messages.noPermission);
                    return false;
                }
                p.getInventory().addItem(new ItemStack[]{RareProperties.itemManager.generateRandomItem(false)});
            }
        }
        return true;
    }
}
