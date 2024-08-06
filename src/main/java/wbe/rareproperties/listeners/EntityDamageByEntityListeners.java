package wbe.rareproperties.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.legendary.Noctis;
import wbe.rareproperties.properties.legendary.Solem;

public class EntityDamageByEntityListeners implements Listener {

    private RareProperties plugin;

    private FileConfiguration config;

    public EntityDamageByEntityListeners(RareProperties plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onHit(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) {
            return;
        }

        LivingEntity damaged = (LivingEntity) event.getEntity();
        Player player = (Player) event.getDamager();

        if(event.getDamage() <= 0.0D) {
            return;
        }

        if(player.getAttackCooldown() < 0.3) {
            return;
        }

        // Comprobación de Solem
        Solem solem = new Solem(plugin);
        boolean solemProperty = solem.checkUse(player, event);
        if(solemProperty) {
            solem.applyEffect(player, event);
        }

        // Comprobación de Noctis
        Noctis noctis = new Noctis(plugin);
        boolean noctisProperty = noctis.checkUse(player, event);
        if(noctisProperty) {
            noctis.applyEffect(player, event);
        }
    }
}
