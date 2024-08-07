package wbe.rareproperties.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.config.Messages;
import wbe.rareproperties.properties.legendary.Armor;

public class EntityDamageListeners implements Listener {

    private RareProperties plugin;

    private FileConfiguration config;

    public EntityDamageListeners(RareProperties plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamaged(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        // Comprobación de Armadura
        Armor armor = new Armor(plugin);
        boolean armorProperty = armor.checkUse(player, event);
        if(armorProperty) {
            armor.applyEffect(player, event);
        }
    }
}
