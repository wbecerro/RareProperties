package wbe.rareproperties.listeners;

import org.bukkit.plugin.PluginManager;
import wbe.rareproperties.RareProperties;

public class EventListeners {

    private RareProperties plugin;

    public EventListeners(RareProperties plugin) {
        this.plugin = plugin;
    }

    public void initializeListeners() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        pluginManager.registerEvents(new EntityDeathListeners(plugin), plugin);
        pluginManager.registerEvents(new InventoryClickListeners(plugin), plugin);
        pluginManager.registerEvents(new PlayerInteractListeners(plugin), plugin);
        pluginManager.registerEvents(new PlayerItemDamageListeners(plugin), plugin);
        pluginManager.registerEvents(new PlayerMoveListeners(plugin), plugin);
        pluginManager.registerEvents(new EntityDamageByEntityListeners(plugin), plugin);
        pluginManager.registerEvents(new CreatureSpawnListeners(plugin), plugin);
        pluginManager.registerEvents(new PlayerQuitListeners(), plugin);
        pluginManager.registerEvents(new EntityPotionEffectListeners(plugin), plugin);
        pluginManager.registerEvents(new EntityShootBowListeners(plugin), plugin);
        pluginManager.registerEvents(new PrepareAnvilListeners(plugin), plugin);
        pluginManager.registerEvents(new PlayerJoinListeners(), plugin);
    }
}
