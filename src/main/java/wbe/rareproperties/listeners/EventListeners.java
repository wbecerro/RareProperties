package wbe.rareproperties.listeners;

import wbe.rareproperties.RareProperties;

public class EventListeners {
    private RareProperties plugin;

    public EventListeners(RareProperties plugin) {
        this.plugin = plugin;
    }

    public void initializeListeners() {
        plugin.getServer().getPluginManager().registerEvents(new EntityDeathListeners(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new InventoryClickListeners(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerInteractListeners(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerItemDamageListeners(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerMoveListeners(plugin), plugin);
    }
}
