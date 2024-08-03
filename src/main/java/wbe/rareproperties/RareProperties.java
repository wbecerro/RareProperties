package wbe.rareproperties;

import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import wbe.rareproperties.commands.CommandListener;
import wbe.rareproperties.listeners.EventListeners;
import wbe.rareproperties.properties.*;
import wbe.rareproperties.util.Scheduler;

import java.util.*;

public class RareProperties extends JavaPlugin {

    private final CommandListener commandListener = new CommandListener(this);

    private final EventListeners eventListeners = new EventListeners(this);

    private final ArrayList<RareProperty> properties = new ArrayList<>(Arrays.asList(new Fly(this), new Repair(this), new Burst(this),
            new Capture(this), new Teleport(this), new Reinforced(this), new Aegis(this), new Demolition(this), new Promptness(this),
            new Propulsion(this)));

    private final ArrayList<EntityType> mobs = new ArrayList<>(Arrays.asList(EntityType.ALLAY, EntityType.ARMADILLO, EntityType.AXOLOTL, EntityType.BAT,
            EntityType.BEE, EntityType.BLAZE, EntityType.BOGGED, EntityType.BREEZE, EntityType.CAMEL, EntityType.CAT, EntityType.CAVE_SPIDER,
            EntityType.CHICKEN, EntityType.COD, EntityType.COW, EntityType.CREEPER, EntityType.DOLPHIN, EntityType.DONKEY, EntityType.DROWNED,
            EntityType.ELDER_GUARDIAN, EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.EVOKER, EntityType.FOX, EntityType.FROG, EntityType.GHAST,
            EntityType.GLOW_SQUID, EntityType.GOAT, EntityType.GUARDIAN, EntityType.HOGLIN, EntityType.HORSE, EntityType.HUSK,
            EntityType.IRON_GOLEM, EntityType.LLAMA, EntityType.MAGMA_CUBE, EntityType.MOOSHROOM, EntityType.MULE, EntityType.OCELOT,
            EntityType.PANDA, EntityType.PARROT, EntityType.PHANTOM, EntityType.PIG, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE,
            EntityType.PILLAGER, EntityType.POLAR_BEAR, EntityType.PUFFERFISH, EntityType.RABBIT, EntityType.RAVAGER,
            EntityType.SALMON, EntityType.SHEEP, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SKELETON_HORSE,
            EntityType.SLIME, EntityType.SNIFFER, EntityType.SNOW_GOLEM, EntityType.SPIDER, EntityType.SQUID, EntityType.STRAY, EntityType.STRIDER,
            EntityType.TADPOLE, EntityType.TRADER_LLAMA, EntityType.TROPICAL_FISH, EntityType.TURTLE, EntityType.VEX, EntityType.VILLAGER,
            EntityType.VINDICATOR, EntityType.WANDERING_TRADER, EntityType.WARDEN, EntityType.WITCH, EntityType.WITHER_SKELETON, EntityType.WOLF,
            EntityType.ZOGLIN, EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN));

    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("RareProperties enabled correctly");
        getCommand("rareproperties").setExecutor(this.commandListener);
        this.eventListeners.initializeListeners();

        Scheduler.startFlyCost(getConfig(), this);
    }

    public void onDisable() {
        reloadConfig();
        getLogger().info("RareProperties disabled correctly");
    }

    public ArrayList<RareProperty> getProperties() {
        return properties;
    }

    public ArrayList<EntityType> getMobs() {
        return mobs;
    }
}

