package wbe.rareproperties.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.uncommon.Instability;

public class EntityShootBowListeners implements Listener {

    private RareProperties plugin;

    public EntityShootBowListeners(RareProperties plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onShootBow(EntityShootBowEvent event) {
        if(event.isCancelled()) {
            return;
        }

        if(!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();

        if(!(event.getProjectile() instanceof Arrow)) {
            return;
        }

        if(event.getBow() == null || event.getBow().getType() == Material.AIR) {
            return;
        }

        // Comprobación de Inestabilidad
        Instability instability = new Instability(plugin);
        boolean instabilityProperty = instability.checkUse(player, event);
        if(instabilityProperty) {
            instability.applyEffect(player, event);
        }
    }
}
