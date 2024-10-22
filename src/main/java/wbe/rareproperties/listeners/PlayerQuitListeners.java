package wbe.rareproperties.listeners;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import wbe.rareproperties.properties.RareProperty;

public class PlayerQuitListeners implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void resetScaleOnQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if(RareProperty.scaleModified.get(player) == null) {
            return;
        }

        if(RareProperty.scaleModified.get(player)) {
            player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1);
            RareProperty.scaleModified.put(player, false);
        }
    }
}
