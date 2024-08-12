package wbe.rareproperties.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import wbe.rareproperties.RareProperties;

public class CreatureSpawnListeners implements Listener {

    RareProperties plugin;

    public CreatureSpawnListeners(RareProperties plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void addRareItemsOnSpawn(CreatureSpawnEvent event) {
        if(event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER)) {

        }
    }
}
