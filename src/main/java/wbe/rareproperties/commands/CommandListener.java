package wbe.rareproperties.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import wbe.rareproperties.RareProperties;
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
                    p.sendMessage(config.getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                for(String x : config.getStringList("Messages.help")) {
                    p.sendMessage(x.replace("&", "§"));
                }
            } else if(args[0].equalsIgnoreCase("list")) {
                if(!p.hasPermission("rareproperties.command.list")) {
                    p.sendMessage(config.getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                p.sendMessage(config.getString("Messages.listMessage").replace("&", "§") + "\n" + String.valueOf(utilities.getValid()));
            } else if(args[0].equalsIgnoreCase("add")) {
                if (!p.hasPermission("rareproperties.command.add")) {
                    p.sendMessage(config.getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                if (args.length != 4) {
                    p.sendMessage(config.getString("Messages.notEnoughArgs").replace("&", "§"));
                    p.sendMessage(config.getString("Messages.addParams").replace("&", "§"));
                    return false;
                }
                ItemStack item = p.getInventory().getItemInMainHand();
                utilities.addProperty(item, args[1], args[2], args[3], p);
                p.updateInventory();
            } else if(args[0].equalsIgnoreCase("remove")) {
                if(!p.hasPermission("rareproperties.command.remove")) {
                    p.sendMessage(config.getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                if(args.length != 2) {
                    p.sendMessage(config.getString("Messages.notEnoughArgs").replace("&", "§"));
                    p.sendMessage(config.getString("Messages.removeParams").replace("&", "§"));
                    return false;
                }
                ItemStack item = p.getInventory().getItemInMainHand();
                utilities.removeProperty(item, args[1], p);
                p.updateInventory();
            } else if(args[0].equalsIgnoreCase("get")) {
                if (!p.hasPermission("rareproperties.command.get")) {
                    p.sendMessage(config.getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                if (args.length != 3) {
                    p.sendMessage(config.getString("Messages.notEnoughArgs").replace("&", "§"));
                    p.sendMessage(config.getString("Messages.getParams").replace("&", "§"));
                    return false;
                }
                utilities.giveProperty(args[1], args[2], p);
            } else if(args[0].equalsIgnoreCase("give")) {
                if(!p.hasPermission("rareproperties.command.give")) {
                    p.sendMessage(config.getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                if(args.length != 4) {
                    p.sendMessage(config.getString("Messages.notEnoughArgs").replace("&", "§"));
                    p.sendMessage(config.getString("Messages.giveParams").replace("&", "§"));
                    return false;
                }
                Player otherPlayer = Bukkit.getServer().getPlayer(args[1]);
                utilities.giveProperty(args[2], args[3], otherPlayer);
            } else if(args[0].equalsIgnoreCase("giveRandom")) {
                if(!sender.hasPermission("rareproperties.command.giveRandom")) {
                    sender.sendMessage(config.getString("Messages.noPermission").replace("&", "§"));
                    return false;
                }
                if(args.length != 3) {
                    sender.sendMessage(config.getString("Messages.notEnoughArgs").replace("&", "§"));
                    sender.sendMessage(config.getString("Messages.giveRandomParams").replace("&", "§"));
                    return false;
                }
                Player otherPlayer = Bukkit.getServer().getPlayer(args[1]);
                utilities.giveRandomProperty(args[2], otherPlayer);
            }
        }
        return true;
    }
}
