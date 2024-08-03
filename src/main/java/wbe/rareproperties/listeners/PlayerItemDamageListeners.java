package wbe.rareproperties.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.Reinforced;

public class PlayerItemDamageListeners implements Listener {

    private RareProperties plugin;

    private FileConfiguration config;

    public PlayerItemDamageListeners(RareProperties plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDurabilityLost(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();

        // Comprobación de Reforzado
        Reinforced reinforced = new Reinforced(plugin);
        boolean reinforcedProperty = reinforced.checkUse(player.getPlayer(), event);
        if(reinforcedProperty) {
            reinforced.applyEffect(player, event);
        }
    }
}
