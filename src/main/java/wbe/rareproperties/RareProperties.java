package wbe.rareproperties;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import wbe.rareproperties.commands.CommandListener;
import wbe.rareproperties.config.Config;
import wbe.rareproperties.config.Messages;
import wbe.rareproperties.items.ItemManager;
import wbe.rareproperties.listeners.EventListeners;
import wbe.rareproperties.properties.RareProperty;
import wbe.rareproperties.properties.common.Enlarge;
import wbe.rareproperties.properties.common.Explosion;
import wbe.rareproperties.properties.common.Shrink;
import wbe.rareproperties.properties.epic.Backstab;
import wbe.rareproperties.properties.epic.Critic;
import wbe.rareproperties.properties.epic.Vampirism;
import wbe.rareproperties.properties.legendary.Armor;
import wbe.rareproperties.properties.legendary.Noctis;
import wbe.rareproperties.properties.legendary.Solem;
import wbe.rareproperties.properties.mythic.*;
import wbe.rareproperties.properties.rare.Cut;
import wbe.rareproperties.properties.rare.Electro;
import wbe.rareproperties.properties.rare.Poison;
import wbe.rareproperties.properties.rare.Wither;
import wbe.rareproperties.properties.uncommon.Fire;
import wbe.rareproperties.properties.uncommon.Freezee;
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

    private RecipeLoader recipeLoader;

    public static ItemManager itemManager;

    private final ArrayList<RareProperty> properties = new ArrayList<>(Arrays.asList(new Fly(this), new Repair(this), new Burst(this),
            new Capture(this), new Teleport(this), new Reinforced(this), new Aegis(this), new Demolition(this), new Promptness(this),
            new Propulsion(this), new Healing(this), new Solem(this), new Noctis(this), new Armor(this), new Vampirism(this),
            new Backstab(this), new Critic(this), new Electro(this), new Cut(this), new Wither(this), new Poison(this),
            new Fire(this), new Freezee(this), new Explosion(this), new Shrink(this), new Enlarge(this), new Adrenaline(this)));

    public void onEnable() {
        saveDefaultConfig();
        createPrefixFile();
        createSuffixFile();
        getLogger().info("RareProperties enabled correctly");
        configuration = getConfig();
        recipeLoader = new RecipeLoader(this);
        itemManager = new ItemManager(this);
        reloadConfiguration(configuration);

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

    public static void reloadConfiguration(FileConfiguration configuration) {
        messages = new Messages(configuration);
        config = new Config(configuration);
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

