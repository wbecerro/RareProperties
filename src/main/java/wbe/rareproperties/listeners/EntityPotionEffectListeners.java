package wbe.rareproperties.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.mythic.Purity;

public class EntityPotionEffectListeners implements Listener {

    private RareProperties plugin;

    public EntityPotionEffectListeners(RareProperties plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamaged(EntityPotionEffectEvent event) {
        if(event.isCancelled()) {
            return;
        }

        if(!event.getAction().equals(EntityPotionEffectEvent.Action.ADDED)) {
            return;
        }

        if(!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();

        // Comprobación de Pureza
        Purity purity = new Purity(plugin);
        boolean purityProperty = purity.checkUse(player, event);
        if(purityProperty) {
            purity.applyEffect(player, event);
        }
    }
}
