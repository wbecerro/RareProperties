package wbe.rareproperties.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.Fly;

public class PlayerMoveListeners implements Listener {

    private RareProperties plugin;

    private FileConfiguration config;

    public PlayerMoveListeners(RareProperties plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerEquip(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Comprobación de Vuelo
        Fly fly = new Fly(plugin);
        boolean flyProperty = fly.checkUse(player, event);
        if(flyProperty) {
            fly.applyEffect(player, event);
        }
    }
}
