package wbe.rareproperties.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import wbe.rareproperties.properties.RareProperty;
import wbe.rareproperties.recipes.RecipeLoader;

import java.util.ArrayList;

public class PlayerJoinListeners implements Listener {

    @EventHandler
    public void discoverRecipesOnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        for(NamespacedKey key : RecipeLoader.keys) {
            player.discoverRecipe(key);
        }

        RareProperty.attributeModified.put(player, new ArrayList<>());
    }
}
