package wbe.rareproperties.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.epic.Repulsion;

public class ProjectileHitListeners implements Listener {

    private RareProperties plugin;

    public ProjectileHitListeners(RareProperties plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event) {
        if(event.isCancelled()) {
            return;
        }

        if(event.getHitEntity() == null) {
            return;
        }

        if(!(event.getHitEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getHitEntity();

        // Comprobación de Repulsión
        Repulsion repulsion = new Repulsion(plugin);
        boolean repulsionProperty = repulsion.checkUse(player, event);
        if(repulsionProperty) {
            repulsion.applyEffect(player, event);
        }
    }
}
