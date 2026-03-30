package wbe.rareproperties.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import wbe.rareproperties.properties.AttributeModifiedPlayer;
import wbe.rareproperties.properties.RareProperty;

public class PlayerQuitListeners implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void resetScaleOnQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if(RareProperty.attributeModified.get(player) == null) {
            return;
        }

        if(!RareProperty.attributeModified.get(player).isEmpty()) {
            for(AttributeModifiedPlayer modifiedPlayer : RareProperty.attributeModified.get(player)) {
                player.getAttribute(modifiedPlayer.getAttribute()).removeModifier(modifiedPlayer.getModifier());
            }

            RareProperty.attributeModified.remove(player);
        }
    }
}
