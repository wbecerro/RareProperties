package wbe.rareproperties.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import wbe.rareproperties.RareProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabListener implements TabCompleter {

    private final List<String> subCommands = Arrays.asList("help", "list", "add", "remove", "get",
            "give", "giveRandom", "socket", "tome", "showItem", "item", "addSocket", "reload");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if(!command.getName().equalsIgnoreCase("RareProperties")) {
            return completions;
        }

        // Mostrar subcomandos
        if(args.length == 1) {
            StringUtil.copyPartialMatches(args[0], subCommands, completions);
        }

        // Argumento 1
        if(args.length == 2) {
            switch(args[0].toLowerCase()) {
                case "remove":
                    if(args[1].isEmpty()) {
                        completions.add("all");
                    } else if("all".startsWith(args[1])) {
                        completions.add("all");
                    }
                case "add":
                case "get":
                    List<String> properties = new ArrayList<>();
                    RareProperties.properties.forEach((property -> {
                        if(args[1].isEmpty()) {
                            properties.add(property.getExternalName());
                        } else if(property.getExternalName().toLowerCase().startsWith(args[1])) {
                            properties.add(property.getExternalName());
                        }
                    }));
                    completions.addAll(properties);
                    break;
                case "give":
                case "giverandom":
                case "socket":
                case "tome":
                case "item":
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        if(args[1].isEmpty()) {
                            completions.add(player.getName());
                        } else if(player.getName().startsWith(args[1])) {
                            completions.add(player.getName());
                        }
                    }
                    break;
                case "addsocket":
                    completions.addAll(RareProperties.config.socketColors);
                    completions.add("<Otro color>");
                    break;
            }
        }

        // Argumento 2
        if(args.length == 3) {
            switch(args[0].toLowerCase()) {
                case "add":
                case "get":
                    completions.add("<Nivel en romano>");
                    break;
                case "give":
                case "giverandom":
                    List<String> properties = new ArrayList<>();
                    RareProperties.properties.forEach((property -> {
                        if(args[2].isEmpty()) {
                            properties.add(property.getExternalName());
                        } else if(property.getExternalName().toLowerCase().startsWith(args[2])) {
                            properties.add(property.getExternalName());
                        }
                    }));
                    completions.addAll(properties);
                    break;
                case "socket":
                    completions.addAll(RareProperties.config.socketColors);
                    completions.add("<Otro color>");
                    break;
                case "item":
                    completions.add("armor");
                    completions.add("weapon");
                    break;
            }
        }

        // Argumento 3
        if(args.length == 4) {
            switch(args[0].toLowerCase()) {
                case "add":
                    completions.add("<Código de color>");
                    break;
                case "give":
                    completions.add("<Nivel en romano>");
                    break;
                case "item":
                    completions.add("socketted");
                    completions.add("unidentified");
                    List<String> rarities = new ArrayList<>();
                    RareProperties.config.itemRarities.forEach((rarity -> {
                        if(args[3].isEmpty()) {
                            rarities.add(rarity.getId());
                        } else if(rarity.getId().startsWith(args[3])) {
                            rarities.add(rarity.getId());
                        }
                    }));
                    completions.addAll(rarities);
            }
        }

        return completions;
    }
}
