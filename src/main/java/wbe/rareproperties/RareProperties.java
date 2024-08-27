package wbe.rareproperties;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import wbe.rareproperties.commands.CommandListener;
import wbe.rareproperties.config.Config;
import wbe.rareproperties.config.Messages;
import wbe.rareproperties.config.Properties;
import wbe.rareproperties.items.ItemManager;
import wbe.rareproperties.listeners.EventListeners;
import wbe.rareproperties.properties.RareProperty;
import wbe.rareproperties.properties.common.*;
import wbe.rareproperties.properties.epic.*;
import wbe.rareproperties.properties.legendary.*;
import wbe.rareproperties.properties.mythic.*;
import wbe.rareproperties.properties.rare.*;
import wbe.rareproperties.properties.uncommon.*;
import wbe.rareproperties.recipes.RecipeLoader;
import wbe.rareproperties.util.Scheduler;

import java.io.File;
import java.util.*;

public class RareProperties extends JavaPlugin {

    private FileConfiguration configuration;

    private File prefixes;
    public static FileConfiguration prefixesConfig;

    private File suffixes;
    public static FileConfiguration suffixesConfig;

    private final CommandListener commandListener = new CommandListener(this);

    private final EventListeners eventListeners = new EventListeners(this);

    public static Messages messages;

    public static Config config;

    public static Properties propertyConfig;

    private RecipeLoader recipeLoader;

    public static ItemManager itemManager;

    private ArrayList<RareProperty> properties;

    public void onEnable() {
        saveDefaultConfig();
        createPrefixFile();
        createSuffixFile();
        getLogger().info("RareProperties enabled correctly");
        reloadConfiguration();
        recipeLoader = new RecipeLoader(this);
        itemManager = new ItemManager(this);

        recipeLoader.loadRecipes();
        getCommand("rareproperties").setExecutor(this.commandListener);
        this.eventListeners.initializeListeners();
        Scheduler.startSchedulers(configuration, this);
    }

    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        recipeLoader.unloadRecipes();
        reloadConfig();
        getLogger().info("RareProperties disabled correctly");
    }

    public ArrayList<RareProperty> getProperties() {
        return properties;
    }

    public void reloadConfiguration() {
        if(!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        reloadConfig();
        configuration = getConfig();
        messages = new Messages(configuration);
        config = new Config(configuration);
        propertyConfig = new Properties(configuration);
        properties = new ArrayList<>(Arrays.asList(new Fly(this), new Repair(this), new Burst(this),
                new Capture(this), new Teleport(this), new Reinforced(this), new Aegis(this), new Demolition(this), new Promptness(this),
                new Propulsion(this), new Healing(this), new Solem(this), new Noctis(this), new Armor(this), new Vampirism(this),
                new Backstab(this), new Critic(this), new Electro(this), new Cut(this), new Wither(this), new Poison(this),
                new Fire(this), new Freezee(this), new Explosion(this), new Shrink(this), new Enlarge(this), new Adrenaline(this),
                new Swarm(this)));
    }

    private void createPrefixFile() {
        prefixes = new File(getDataFolder(), "prefixes.yml");
        if(!prefixes.exists()) {
            prefixes.getParentFile().mkdirs();
            saveResource("prefixes.yml", false);
        }

        prefixesConfig = YamlConfiguration.loadConfiguration(prefixes);
    }

    private void createSuffixFile() {
        suffixes = new File(getDataFolder(), "suffixes.yml");
        if(!suffixes.exists()) {
            suffixes.getParentFile().mkdirs();
            saveResource("suffixes.yml", false);
        }

        suffixesConfig = YamlConfiguration.loadConfiguration(suffixes);
    }
}

