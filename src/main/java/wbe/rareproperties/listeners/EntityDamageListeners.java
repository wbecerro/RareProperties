package wbe.rareproperties.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import wbe.rareproperties.RareProperties;
import wbe.rareproperties.properties.legendary.Armor;

public class EntityDamageListeners implements Listener {

    private RareProperties plugin;


    public EntityDamageListeners(RareProperties plugin) {
        this.plugin = plugin;
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
