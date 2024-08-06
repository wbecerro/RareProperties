package wbe.rareproperties.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.mythic.Capture;

public class EntityDeathListeners implements Listener {

    private RareProperties plugin;

    private FileConfiguration config;

    public EntityDeathListeners(RareProperties plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMobDeath(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if(player == null) {
            return;
        }

        // Comprobación de Captura
        Capture capture = new Capture(plugin);
        boolean captureProperty = capture.checkUse(player, event);
        if(captureProperty) {
            capture.applyEffect(player, event);
        }
    }
}
